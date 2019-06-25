package com.am.server.api.admin.role.service.impl;

import com.am.server.api.admin.role.dao.jpa.RoleDao;
import com.am.server.api.admin.role.dao.jpa.RoleMenuDao;
import com.am.server.api.admin.role.pojo.ao.RoleListAO;
import com.am.server.api.admin.role.pojo.ao.SaveRoleAO;
import com.am.server.api.admin.role.pojo.ao.UpdateMenuAO;
import com.am.server.api.admin.role.pojo.ao.UpdateRoleAO;
import com.am.server.api.admin.role.pojo.po.QRoleMenuPO;
import com.am.server.api.admin.role.pojo.po.QRolePO;
import com.am.server.api.admin.role.pojo.po.RoleMenuPO;
import com.am.server.api.admin.role.pojo.po.RolePO;
import com.am.server.api.admin.role.pojo.vo.RoleListVo;
import com.am.server.api.admin.role.pojo.vo.SelectRoleVO;
import com.am.server.api.admin.role.service.RoleService;
import com.am.server.api.admin.user.pojo.po.QAdminUserPO;
import com.am.server.api.admin.user.service.UserPermissionCacheService;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.base.service.CommonService;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.IdUtils;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 阮雪峰
 * @date 2018/7/27 10:47
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    private final RoleMenuDao roleMenuDao;

    private final UserPermissionCacheService userPermissionCacheService;

    private final CommonService commonService;

    @PersistenceContext
    private EntityManager entityManager;

    public RoleServiceImpl(RoleDao roleDao, RoleMenuDao roleMenuDao, UserPermissionCacheService userPermissionCacheService, CommonService commonService) {
        this.roleDao = roleDao;
        this.roleMenuDao = roleMenuDao;
        this.userPermissionCacheService = userPermissionCacheService;
        this.commonService = commonService;
    }

    @ReadOnly
    @Override
    public PageVO<RoleListVo> list(RoleListAO roleListAo) {
        PageVO<RoleListVo> page = new PageVO<RoleListVo>().setPage(roleListAo.getPage()).setPageSize(roleListAo.getPageSize());

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QRolePO qRole = QRolePO.rolePO;
        QAdminUserPO qAdminUser = QAdminUserPO.adminUserPO;
        JPAQuery<Tuple> query = queryFactory.select(
                qRole.id,
                qRole.name,
                qRole.describe,
                qRole.createTime,
                qAdminUser.name.as(Constant.CREATOR_NAME)
        )
                .from(qRole)
                .leftJoin(qAdminUser)
                .on(qRole.creator.eq(qAdminUser.id))
                .offset(page.getCol())
                .limit(page.getPageSize())
                .orderBy(qRole.id.desc());


        Optional.of(roleListAo)
                .map(RoleListAO::getName)
                .filter(name -> !name.isEmpty())
                .ifPresent(name -> query.where(qRole.name.like("%" + name + "%")));

        List<RoleListVo> list = query.fetch()
                .stream()
                .map(
                        tuple -> new RoleListVo()
                                .setId(tuple.get(qRole.id))
                                .setName(tuple.get(qRole.name))
                                .setDescribe(tuple.get(qRole.name))
                                .setCreateTime(tuple.get(qRole.createTime))
                                .setCreatorName(tuple.get(qAdminUser.name.as(Constant.CREATOR_NAME)))

                ).collect(Collectors.toList());

        page.setTotal((int) query.fetchCount());
        page.setRows(list);
        return page;
    }

    @Commit
    @Override
    public void save(SaveRoleAO roleAo) {
        RolePO role = new RolePO()
                .setId(IdUtils.getId())
                .setName(roleAo.getName())
                .setDescribe(roleAo.getDescribe())
                .setCreateTime(LocalDateTime.now())
                .setCreator(commonService.getLoginUserId());
        roleDao.save(role);
    }

    @Commit
    @Override
    public void update(UpdateRoleAO roleAo) {
        RolePO role = new RolePO()
                .setId(roleAo.getId())
                .setName(roleAo.getName())
                .setDescribe(roleAo.getDescribe())
                .setCreateTime(LocalDateTime.now())
                .setCreator(commonService.getLoginUserId());
        roleDao.save(role);
    }

    @Commit
    @Override
    public void delete(Long id) {
        roleDao.deleteById(id);
        roleDao.deleteRelateUserById(id);
        roleDao.deleteRelateMenuById(id);
        userPermissionCacheService.removeAll();
    }

    @ReadOnly
    @Override
    public List<SelectRoleVO> findAll() {
        return roleDao.findAll(Sort.by(Sort.Order.desc("id")))
                .stream()
                .map(rolePo -> new SelectRoleVO().setId(rolePo.getId()).setName(rolePo.getName()))
                .collect(Collectors.toList());
    }

    @Commit
    @Override
    public void updateMenuList(UpdateMenuAO updateMenuAo) {
        roleMenuDao.deleteByRole(updateMenuAo.getId());
        Optional.ofNullable(updateMenuAo.getMenuList())
                .filter(list -> !list.isEmpty())
                .ifPresent(list -> roleMenuDao.saveAll(
                        list.stream()
                                .map(item -> new RoleMenuPO(IdUtils.getId(), updateMenuAo.getId(), item))
                                .collect(Collectors.toList())
                ));
    }

    @Override
    public List<Long> getMenuList(Long id) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QRoleMenuPO qRoleMenu = QRoleMenuPO.roleMenuPO;

        return queryFactory.select(qRoleMenu.menu)
                .from(qRoleMenu)
                .where(qRoleMenu.role.eq(id))
                .fetch();
    }
}
