package com.am.server.api.admin.user.service.impl;

import com.am.server.api.admin.upload.service.FileUploadService;
import com.am.server.api.admin.user.dao.jpa.AdminUserDao;
import com.am.server.api.admin.user.dao.jpa.UserRoleDao;
import com.am.server.api.admin.user.pojo.AdminUser;
import com.am.server.api.admin.user.pojo.QAdminUser;
import com.am.server.api.admin.user.pojo.QUserRole;
import com.am.server.api.admin.user.pojo.UserRole;
import com.am.server.api.admin.user.pojo.param.SaveAdminUserAO;
import com.am.server.api.admin.user.pojo.param.ListQuery;
import com.am.server.api.admin.user.pojo.param.LoginQuery;
import com.am.server.api.admin.user.pojo.param.UpdateAdminUserAO;
import com.am.server.api.admin.user.service.AdminUserService;
import com.am.server.api.admin.user.service.UserPermissionCacheService;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.entity.PageVO;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.DesUtils;
import com.am.server.common.util.FileUtils;
import com.am.server.common.util.IdUtils;
import com.am.server.common.util.StringUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @PersistenceContext
    private EntityManager entityManager;

    public AdminUserServiceImpl(UserPermissionCacheService userPermissionCacheService, AdminUserDao adminUserDao, UserRoleDao userRoleDao, FileUploadService fileUploadService) {
        this.userPermissionCacheService = userPermissionCacheService;
        this.adminUserDao = adminUserDao;
        this.userRoleDao = userRoleDao;
        this.fileUploadService = fileUploadService;
    }

    @Override
    public AdminUser login(LoginQuery query) {
        return adminUserDao.findByUsername(query.getUsername()).orElse(null);
    }

    @ReadOnly
    @Override
    public AdminUser info(Long id) {
        Optional<AdminUser> userOptional = adminUserDao.findById(id);
        userOptional.ifPresent(user -> user.setMenuList(adminUserDao.findMenuList(user.getId())));
        return userOptional.orElse(null);
    }

    @ReadOnly
    @Override
    public PageVO<AdminUser> list(ListQuery listQuery) {
        PageVO<AdminUser> page = new PageVO<AdminUser>().setPageSize(listQuery.getPageSize()).setPage(listQuery.getPage());

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QAdminUser qAdminUser = QAdminUser.adminUser;
        QAdminUser creator = new QAdminUser("creator");

        JPAQuery<AdminUser> query = queryFactory.select(
                Projections.bean(
                        AdminUser.class,
                        qAdminUser.id,
                        qAdminUser.username,
                        qAdminUser.name,
                        qAdminUser.email,
                        qAdminUser.avatar,
                        qAdminUser.gender,
                        qAdminUser.createTime,
                        creator.name.as(Constant.CREATOR_NAME)
                )
        )
                .from(qAdminUser)
                .leftJoin(creator).on(qAdminUser.creator.eq(creator.id))
                .offset(page.getCol())
                .limit(listQuery.getPageSize())
                .orderBy(qAdminUser.id.desc());

        Optional<ListQuery> userOptional = Optional.of(listQuery);

        userOptional.map(ListQuery::getName)
                .filter(name -> !name.isEmpty())
                .ifPresent(name -> query.where(qAdminUser.name.like("%" + name + "%")));

        userOptional.map(ListQuery::getEmail)
                .filter(email -> !email.isEmpty())
                .ifPresent(email -> query.where(qAdminUser.email.like("%" + email + "%")));

        userOptional.map(ListQuery::getUsername)
                .filter(username -> !username.isEmpty())
                .ifPresent(username -> query.where(qAdminUser.username.like("%" + username + "%")));

        page.setTotal((int) query.fetchCount()).setRows(query.fetch());
        return page;
    }

    @Commit
    @Override
    public void save(SaveAdminUserAO adminUser) {
        String key = StringUtils.getRandomStr(RANDOM_STRING_LENGTH);
        Long id = IdUtils.getId();
        AdminUser user = new AdminUser()
                .setId(id)
                .setUsername(adminUser.getUsername())
                .setCreateTime(LocalDateTime.now())
                .setEmail(adminUser.getEmail())
                .setGender(adminUser.getGender())
                .setAvatar(uploadAvatar(id, adminUser.getImg()))
                .setKey(key)
                .setPassword(DesUtils.encrypt(Constant.INITIAL_PASSWORD, key));

        adminUserDao.save(user);
    }

    /**
     * 上传头像图片
     *
     * @param user   user
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
    public void delete(AdminUser user) {
        adminUserDao.deleteById(user.getId());
        userPermissionCacheService.remove(user.getId());
    }

    @ReadOnly
    @Override
    public Boolean isEmailExist(String email) {
        return adminUserDao.findByEmail(email).isPresent();
    }

    @Commit
    @Override
    public void resetPassword(AdminUser user) {
        String key = StringUtils.getRandomStr(RANDOM_STRING_LENGTH);
        String password = DesUtils.encrypt(Constant.INITIAL_PASSWORD, key);

        adminUserDao.resetPassword(user.getId(), key, password);
    }

    @Commit
    @Override
    public void updateRole(AdminUser user) {

        //删除用户角色关联
        userRoleDao.deleteByUser(user.getId());
        Optional.of(user)
                .map(AdminUser::getRoleIdList)
                .filter(list -> list.size() > 0)
                .ifPresent(list -> {

                    //重新关联
                    List<UserRole> userRoles = new ArrayList<>(list.size());
                    list.forEach(item -> userRoles.add(new UserRole(IdUtils.getId(), user.getId(), item)));

                    userRoleDao.saveAll(userRoles);
                    userPermissionCacheService.remove(user.getId());
                });
    }

    @Commit
    @Override
    public void update(UpdateAdminUserAO user) {
        QAdminUser qAdminUser = QAdminUser.adminUser;
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
    public AdminUser findById(Long id) {
        return adminUserDao.findById(id).orElse(null);
    }

    @Override
    public List<Long> findRoleIdList(AdminUser user) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QUserRole qUserRole = QUserRole.userRole;

        return queryFactory.select(qUserRole.role)
                .from(qUserRole)
                .where(qUserRole.user.eq(user.getId()))
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
