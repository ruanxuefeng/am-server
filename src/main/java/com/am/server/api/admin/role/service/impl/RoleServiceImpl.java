package com.am.server.api.admin.role.service.impl;

import com.am.server.advice.update.annotation.Save;
import com.am.server.api.admin.role.dao.jpa.RoleDao;
import com.am.server.api.admin.role.dao.jpa.RoleMenuDao;
import com.am.server.api.admin.role.entity.QRole;
import com.am.server.api.admin.role.entity.QRoleMenu;
import com.am.server.api.admin.role.entity.Role;
import com.am.server.api.admin.role.entity.RoleMenu;
import com.am.server.api.admin.role.service.RoleService;
import com.am.server.api.admin.user.pojo.po.QAdminUserPO;
import com.am.server.api.admin.user.service.UserPermissionCacheService;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.IdUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author 阮雪峰
 * @date 2018/7/27 10:47
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    private final RoleMenuDao roleMenuDao;

    private final UserPermissionCacheService userPermissionCacheService;

    @PersistenceContext
    private EntityManager entityManager;

    public RoleServiceImpl(RoleDao roleDao, RoleMenuDao roleMenuDao, UserPermissionCacheService userPermissionCacheService) {
        this.roleDao = roleDao;
        this.roleMenuDao = roleMenuDao;
        this.userPermissionCacheService = userPermissionCacheService;
    }

    @ReadOnly
    @Override
    public void list(PageVO<Role> page, Role role) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QRole qRole = QRole.role;
        QAdminUserPO qAdminUser = QAdminUserPO.adminUserPO;
        JPAQuery<Role> query = queryFactory.select(
                Projections.bean(
                        Role.class,
                        qRole.id,
                        qRole.name,
                        qRole.describe,
                        qRole.createTime,
                        qAdminUser.name.as(Constant.CREATOR_NAME)
                )
        )
                .from(qRole)
                .leftJoin(qAdminUser)
                .on(qRole.creator.eq(qAdminUser.id))
                .offset(page.getCol())
                .limit(page.getPageSize())
                .orderBy(qRole.id.desc());


        Optional.of(role)
                .map(Role::getName)
                .filter(name -> !name.isEmpty())
                .ifPresent(name -> query.where(qRole.name.like("%" + name + "%")));

        List<Role> list = query.fetch();
        page.setTotal((int) query.fetchCount());
        page.setRows(list);
    }

    @Save
    @Commit
    @Override
    public void save(Role role) {
        roleDao.save(role);
    }

    @Commit
    @Override
    public void update(Role role) {
        roleDao.save(role);
    }

    @Commit
    @Override
    public void delete(Role role) {
        roleDao.deleteById(role.getId());
        roleDao.deleteRelateUserById(role.getId());
        roleDao.deleteRelateMenuById(role.getId());
        userPermissionCacheService.removeAll();
    }

    @ReadOnly
    @Override
    public List<Role> findAll() {
        return roleDao.findAll(Sort.by(Sort.Order.desc("id")));
    }

    @Commit
    @Override
    public void updateMenuList(Role role) {
        roleMenuDao.deleteByRole(role.getId());
        Optional.ofNullable(role.getMenuList())
                .filter(list -> !list.isEmpty())
                .ifPresent(list -> roleMenuDao.saveAll(
                        list.stream()
                                .map(item -> new RoleMenu(IdUtils.getId(), role.getId(), item))
                                .collect(Collectors.toList())
                ));
    }

    @Override
    public List<Long> getMenuList(Role role) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QRoleMenu qRoleMenu = QRoleMenu.roleMenu;

        return queryFactory.select(qRoleMenu.menu)
                .from(qRoleMenu)
                .where(qRoleMenu.role.eq(role.getId()))
                .fetch();
    }
}
