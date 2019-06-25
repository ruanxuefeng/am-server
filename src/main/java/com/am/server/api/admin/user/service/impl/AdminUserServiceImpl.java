package com.am.server.api.admin.user.service.impl;

import com.am.server.api.admin.role.pojo.po.RolePO;
import com.am.server.api.admin.upload.service.FileUploadService;
import com.am.server.api.admin.user.dao.jpa.AdminUserDao;
import com.am.server.api.admin.user.dao.jpa.UserRoleDao;
import com.am.server.api.admin.user.exception.PasswordErrorException;
import com.am.server.api.admin.user.exception.UserNotExistException;
import com.am.server.api.admin.user.pojo.ao.*;
import com.am.server.api.admin.user.pojo.po.AdminUserPO;
import com.am.server.api.admin.user.pojo.po.QAdminUserPO;
import com.am.server.api.admin.user.pojo.po.QUserRolePO;
import com.am.server.api.admin.user.pojo.po.UserRolePO;
import com.am.server.api.admin.user.pojo.vo.AdminUserListVO;
import com.am.server.api.admin.user.pojo.vo.LoginUserInfoVO;
import com.am.server.api.admin.user.pojo.vo.UserInfoVO;
import com.am.server.api.admin.user.service.AdminUserService;
import com.am.server.api.admin.user.service.UserPermissionCacheService;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.base.service.CommonService;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.*;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 阮雪峰
 * @date 2018/7/24 17:23
 */
@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService {

    /**
     * 密码加密key的长度，
     */
    private static final int RANDOM_STRING_LENGTH = 512;
    /**
     * 存储头像的相对路径
     */
    private static final String AVATAR_FOLDER = "/avatar/";

    private final AdminUserDao adminUserDao;

    private final UserPermissionCacheService userPermissionCacheService;

    private final UserRoleDao userRoleDao;

    private final FileUploadService fileUploadService;

    private final CommonService commonService;

    @PersistenceContext
    private EntityManager entityManager;

    public AdminUserServiceImpl(UserPermissionCacheService userPermissionCacheService, AdminUserDao adminUserDao, UserRoleDao userRoleDao,
                                FileUploadService fileUploadService, CommonService commonService) {
        this.userPermissionCacheService = userPermissionCacheService;
        this.adminUserDao = adminUserDao;
        this.userRoleDao = userRoleDao;
        this.fileUploadService = fileUploadService;
        this.commonService = commonService;
    }

    @Override
    public LoginUserInfoVO login(LoginAO query) {
        Optional<AdminUserPO> optional = adminUserDao.findByUsername(query.getUsername());
        optional.orElseThrow(UserNotExistException::new);

        return optional
                .filter(user -> {
                    //校验密码是否一样
                    String inputPassword = new String(Base64.getDecoder().decode(query.getPassword().getBytes()));
                    String userPassword = DesUtils.decrypt(user.getPassword(), user.getKey());
                    return inputPassword.equals(userPassword);
                })
                .map(user -> new LoginUserInfoVO(JwtUtils.sign(user.getId().toString()), adminUserDao.findMenuList(user.getId())))
                .orElseThrow(PasswordErrorException::new);
    }

    @ReadOnly
    @Override
    public UserInfoVO info(Long id) {
        return adminUserDao.findById(id)
                .map(user -> new UserInfoVO()
                        .setMenus(adminUserDao.findMenuList(user.getId()))
                        .setAvatar(user.getAvatar())
                        .setEmail(user.getEmail())
                        .setGender(user.getGender())
                        .setName(user.getName())
                        .setRoles(user.getRoleList().stream().map(RolePO::getName).collect(Collectors.toList()))
                )
                .orElse(null);
    }

    @ReadOnly
    @Override
    public PageVO<AdminUserListVO> list(AdminUserListAO listQuery) {
        PageVO<AdminUserListVO> page = new PageVO<AdminUserListVO>().setPageSize(listQuery.getPageSize()).setPage(listQuery.getPage());

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QAdminUserPO qAdminUser = QAdminUserPO.adminUserPO;
        QAdminUserPO creator = new QAdminUserPO("creator");

        JPAQuery<Tuple> query = queryFactory.select(
                qAdminUser.id,
                qAdminUser.username,
                qAdminUser.name,
                qAdminUser.email,
                qAdminUser.avatar,
                qAdminUser.gender,
                qAdminUser.createTime,
                creator.name.as(Constant.CREATOR_NAME)
        )
                .from(qAdminUser)
                .leftJoin(creator).on(qAdminUser.creator.eq(creator.id))
                .offset(page.getCol())
                .limit(listQuery.getPageSize())
                .orderBy(qAdminUser.id.desc());

        Optional<AdminUserListAO> userOptional = Optional.of(listQuery);

        userOptional.map(AdminUserListAO::getName)
                .filter(name -> !name.isEmpty())
                .ifPresent(name -> query.where(qAdminUser.name.like("%" + name + "%")));

        userOptional.map(AdminUserListAO::getEmail)
                .filter(email -> !email.isEmpty())
                .ifPresent(email -> query.where(qAdminUser.email.like("%" + email + "%")));

        userOptional.map(AdminUserListAO::getUsername)
                .filter(username -> !username.isEmpty())
                .ifPresent(username -> query.where(qAdminUser.username.like("%" + username + "%")));

        List<AdminUserListVO> list = query.fetch()
                .stream()
                .map(tuple -> new AdminUserListVO()
                        .setAvatar(tuple.get(qAdminUser.avatar))
                        .setCreatorName(tuple.get(creator.name.as(Constant.CREATOR_NAME)))
                        .setEmail(tuple.get(qAdminUser.email))
                        .setGender(tuple.get(qAdminUser.gender))
                        .setName(tuple.get(qAdminUser.name))
                        .setId(tuple.get(qAdminUser.id))
                        .setUsername(tuple.get(qAdminUser.username))
                        .setCreateTime(tuple.get(qAdminUser.createTime))
                )
                .collect(Collectors.toList());
        page.setTotal((int) query.fetchCount()).setRows(list);
        return page;
    }

    @Commit
    @Override
    public void save(SaveAdminUserAO adminUser) {
        String key = StringUtils.getRandomStr(RANDOM_STRING_LENGTH);
        Long id = IdUtils.getId();
        AdminUserPO user = new AdminUserPO()
                .setId(id)
                .setUsername(adminUser.getUsername())
                .setName(adminUser.getName())
                .setCreateTime(LocalDateTime.now())
                .setEmail(adminUser.getEmail())
                .setGender(adminUser.getGender())
                .setAvatar(uploadAvatar(id, adminUser.getImg()))
                .setCreator(commonService.getLoginUserId())
                .setKey(key)
                .setPassword(DesUtils.encrypt(Constant.INITIAL_PASSWORD, key));

        adminUserDao.save(user);
    }

    /**
     * 上传头像图片
     *
     * @param id     id
     * @param avatar 头像文件
     * @author 阮雪峰
     * @date 2019/2/18 14:31
     */
    private String uploadAvatar(Long id, MultipartFile avatar) {
        String key = AVATAR_FOLDER + id
                + FileUtils.getFileSuffix(Objects.requireNonNull(avatar.getOriginalFilename()));

        return fileUploadService.upload(avatar, key);
    }

    @Commit
    @Override
    public void delete(Long id) {
        adminUserDao.deleteById(id);
        userPermissionCacheService.remove(id);
    }

    @ReadOnly
    @Override
    public Boolean isEmailExist(String email) {
        return adminUserDao.findByEmail(email).isPresent();
    }

    @Commit
    @Override
    public void resetPassword(Long id) {
        String key = StringUtils.getRandomStr(RANDOM_STRING_LENGTH);
        String password = DesUtils.encrypt(Constant.INITIAL_PASSWORD, key);

        QAdminUserPO qAdminUser = QAdminUserPO.adminUserPO;
        new JPAUpdateClause(entityManager, qAdminUser)
                .set(qAdminUser.password, password)
                .set(qAdminUser.key, key)
                .where(qAdminUser.id.eq(id))
                .execute();
    }

    @Commit
    @Override
    public void updateRole(Long id, List<Long> roleIdList) {

        //删除用户角色关联
        userRoleDao.deleteByUser(id);
        Optional.ofNullable(roleIdList)
                .ifPresent(list -> {
                    //重新关联
                    List<UserRolePO> userRoles = new ArrayList<>(list.size());
                    list.forEach(item -> userRoles.add(new UserRolePO(IdUtils.getId(), id, item)));

                    userRoleDao.saveAll(userRoles);
                    userPermissionCacheService.remove(id);
                });
    }

    @Commit
    @Override
    public void update(UpdateAdminUserAO user) {
        QAdminUserPO qAdminUser = QAdminUserPO.adminUserPO;
        JPAUpdateClause jpaUpdateClause = new JPAUpdateClause(entityManager, qAdminUser);
        if (user.getImg() != null && !user.getImg().isEmpty()) {
            jpaUpdateClause.set(qAdminUser.avatar, uploadAvatar(user.getId(), user.getImg()));
        }

        jpaUpdateClause.set(qAdminUser.email, user.getEmail())
                .set(qAdminUser.name, user.getName())
                .set(qAdminUser.gender, user.getGender())
                .set(qAdminUser.username, user.getUsername())
                .where(qAdminUser.id.eq(user.getId()))
                .execute();
    }

    @Override
    public void update(UpdateUserInfoAO user) {
        QAdminUserPO qAdminUser = QAdminUserPO.adminUserPO;
        JPAUpdateClause jpaUpdateClause = new JPAUpdateClause(entityManager, qAdminUser);
        if (user.getImg() != null && !user.getImg().isEmpty()) {
            jpaUpdateClause.set(qAdminUser.avatar, uploadAvatar(user.getId(), user.getImg()));
        }

        jpaUpdateClause.set(qAdminUser.email, user.getEmail())
                .set(qAdminUser.name, user.getName())
                .set(qAdminUser.gender, user.getGender())
                .where(qAdminUser.id.eq(user.getId()))
                .execute();
    }

    @Override
    public AdminUserPO findById(Long id) {
        return adminUserDao.findById(id).orElse(null);
    }

    @Override
    public List<Long> findRoleIdList(Long id) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QUserRolePO qUserRole = QUserRolePO.userRolePO;

        return queryFactory.select(qUserRole.role)
                .from(qUserRole)
                .where(qUserRole.user.eq(id))
                .fetch();
    }

    @Override
    public Boolean isUsernameExist(String username) {
        return adminUserDao.findByUsername(username).isPresent();
    }

    @Override
    public Boolean isEmailExistWithId(String email, Long id) {
        return adminUserDao.findByIdNotAndEmail(id, email).isPresent();
    }

    @Override
    public Boolean isUsernameExistWithId(String username, Long id) {
        return adminUserDao.findByIdNotAndUsername(id, username).isPresent();
    }
}
