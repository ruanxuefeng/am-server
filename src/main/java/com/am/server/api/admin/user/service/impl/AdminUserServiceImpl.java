package com.am.server.api.admin.user.service.impl;

import com.am.server.api.admin.upload.service.FileUploadService;
import com.am.server.api.admin.user.dao.jpa.AdminUserDao;
import com.am.server.api.admin.user.dao.jpa.UserRoleDao;
import com.am.server.api.admin.user.entity.AdminUser;
import com.am.server.api.admin.user.entity.QAdminUser;
import com.am.server.api.admin.user.entity.QUserRole;
import com.am.server.api.admin.user.entity.UserRole;
import com.am.server.api.admin.user.service.AdminUserService;
import com.am.server.advice.update.annotation.Save;
import com.am.server.api.admin.user.service.UserPermissionCacheService;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.page.Page;
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
    public AdminUser login(AdminUser user) {
        return adminUserDao.findByUsername(user.getUsername()).orElse(null);
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
    public void list(Page<AdminUser> page, AdminUser user) {
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
                .limit(page.getPageSize())
                .orderBy(qAdminUser.id.desc());

        Optional<AdminUser> userOptional = Optional.of(user);

        userOptional.map(AdminUser::getName)
                .filter(name -> !name.isEmpty())
                .ifPresent(name -> query.where(qAdminUser.name.like("%" + name + "%")));

        userOptional.map(AdminUser::getEmail)
                .filter(email -> !email.isEmpty())
                .ifPresent(email -> query.where(qAdminUser.email.like("%" + email + "%")));

        userOptional.map(AdminUser::getUsername)
                .filter(username -> !username.isEmpty())
                .ifPresent(username -> query.where(qAdminUser.username.like("%" + username + "%")));


        page.setTotal((int) query.fetchCount());
        page.setRows(query.fetch());
    }

    @Save
    @Commit
    @Override
    public void save(AdminUser user, MultipartFile img) {

        uploadAvatar(user, img);

        user.setKey(StringUtils.getRandomStr(RANDOM_STRING_LENGTH));
        user.setPassword(DesUtils.encrypt(Constant.INITIAL_PASSWORD, user.getKey()));

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
    private void uploadAvatar(AdminUser user, MultipartFile avatar) {
        String key = AVATAR_FOLDER + user.getId()
                + FileUtils.getFileSuffix(Objects.requireNonNull(avatar.getOriginalFilename()));

        user.setAvatar(fileUploadService.upload(avatar, key));
    }

    @Commit
    @Override
    public void delete(AdminUser user) {
        adminUserDao.deleteById(user.getId());
        userPermissionCacheService.remove(user.getId());
    }

    @ReadOnly
    @Override
    public boolean isEmailExist(AdminUser user) {
        if (Optional.of(user).map(AdminUser::getId).isPresent()) {
            return adminUserDao.findByIdNotAndEmail(user.getId(), user.getEmail()).isPresent();
        } else {
            return adminUserDao.findByEmail(user.getEmail()).isPresent();
        }
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
    public void update(AdminUser user, MultipartFile img) {
        QAdminUser qAdminUser = QAdminUser.adminUser;
        JPAUpdateClause jpaUpdateClause = new JPAUpdateClause(entityManager, qAdminUser);
        if (img != null && !img.isEmpty()) {
            uploadAvatar(user, img);
            jpaUpdateClause.set(qAdminUser.avatar, user.getAvatar());
        }

        jpaUpdateClause.set(qAdminUser.email, user.getEmail())
                .set(qAdminUser.name, user.getName())
                .set(qAdminUser.gender, user.getGender())
                .set(qAdminUser.username, user.getUsername())
                .where(qAdminUser.id.eq(user.getId()))
                .execute();

//        adminUserDao.save(user);
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
    public Boolean isUsernameExist(AdminUser user) {
        if (Optional.of(user).map(AdminUser::getId).isPresent()) {
            return adminUserDao.findByIdNotAndUsername(user.getId(), user.getUsername()).isPresent();
        } else {
            return adminUserDao.findByUsername(user.getUsername()).isPresent();
        }
    }
}
