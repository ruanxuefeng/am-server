package com.am.server.api.admin.menu.service.impl;

import com.am.server.api.admin.menu.dao.jpa.MenuDao;
import com.am.server.api.admin.menu.pojo.ao.MenuListAO;
import com.am.server.api.admin.menu.pojo.ao.SaveMenuAO;
import com.am.server.api.admin.menu.pojo.ao.UpdateMenuAO;
import com.am.server.api.admin.menu.pojo.po.MenuPO;
import com.am.server.api.admin.menu.pojo.po.QMenuPO;
import com.am.server.api.admin.menu.pojo.vo.MenuListVO;
import com.am.server.api.admin.menu.pojo.vo.TreeMenuVO;
import com.am.server.api.admin.menu.service.MenuService;
import com.am.server.api.admin.role.pojo.vo.SelectRoleVO;
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
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 阮雪峰
 * @date 2018/7/30 15:17
 */
@Service("menuService")
public class MenuServiceImpl implements MenuService {

    private final MenuDao menuDao;

    private final UserPermissionCacheService userPermissionCacheService;

    private final CommonService commonService;

    @PersistenceContext
    private EntityManager entityManager;

    public MenuServiceImpl(MenuDao menuDao, UserPermissionCacheService userPermissionCacheService, CommonService commonService) {
        this.menuDao = menuDao;
        this.userPermissionCacheService = userPermissionCacheService;
        this.commonService = commonService;
    }

    @ReadOnly
    @Override
    public PageVO<MenuListVO> list(MenuListAO menuListAo) {
        PageVO<MenuListVO> page = new PageVO<MenuListVO>().setPage(menuListAo.getPage()).setPageSize(menuListAo.getPageSize());

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QMenuPO menu = QMenuPO.menuPO;
        QMenuPO parent = new QMenuPO("parent");
        QAdminUserPO creator = QAdminUserPO.adminUserPO;
        JPAQuery<Tuple> query = queryFactory.select(
                menu.id,
                menu.name,
                menu.key,
                menu.createTime,
                menu.pid,
                creator.name.as(Constant.CREATOR_NAME),
                parent.name.as("parentName")
        )
                .from(menu)
                .leftJoin(creator).on(menu.creator.eq(creator.id))
                .leftJoin(parent).on(menu.pid.eq(parent.id))
                .offset(page.getCol())
                .limit(page.getPageSize())
                .orderBy(menu.id.desc());

        Optional<MenuListAO> menuOptional = Optional.of(menuListAo);

        menuOptional.map(MenuListAO::getName)
                .filter(name -> !name.isEmpty())
                .ifPresent(name -> query.where(menu.name.like("%" + name + "%")));

        menuOptional.map(MenuListAO::getKey)
                .filter(key -> !key.isEmpty())
                .ifPresent(key -> query.where(menu.key.like("%" + key + "%")));

        List<MenuListVO> list = query.fetch()
                .stream().map(
                        tuple -> new MenuListVO()
                                .setId(tuple.get(menu.id))
                                .setName(tuple.get(menu.name))
                                .setKey(tuple.get(menu.key))
                                .setPid(tuple.get(menu.pid))
                                .setCreateTime(tuple.get(menu.createTime))
                                .setParentName(tuple.get(parent.name.as("parentName")))
                                .setCreatName(tuple.get(creator.name.as(Constant.CREATOR_NAME)))
                ).collect(Collectors.toList());
        page.setTotal((int) query.fetchCount());
        page.setRows(list);

        return page;
    }

    @Commit
    @Override
    public void save(SaveMenuAO menu) {
        MenuPO menupo = new MenuPO()
                .setId(IdUtils.getId())
                .setName(menu.getName())
                .setKey(menu.getKey())
                .setCreator(commonService.getLoginUserId())
                .setCreateTime(LocalDateTime.now())
                .setPid(menu.getPid())
                .setLevel(menu.getPid() == null ? 1 : 2);

        menuDao.save(menupo);
    }

    @Commit
    @Override
    public void update(UpdateMenuAO menu) {
        MenuPO menupo = new MenuPO()
                .setId(menu.getId())
                .setName(menu.getName())
                .setKey(menu.getKey())
                .setPid(menu.getPid())
                .setLevel(menu.getPid() == null ? 1 : 2);

        menuDao.save(menupo);
    }

    @Commit
    @Override
    public void delete(Long id) {
        menuDao.deleteById(id);
        menuDao.deleteRelateRoles(id);
        userPermissionCacheService.removeAll();
    }

    @ReadOnly
    @Override
    public List<SelectRoleVO> parentList() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QMenuPO qMenu = QMenuPO.menuPO;
        return queryFactory.select(qMenu.id, qMenu.name)
                .from(qMenu)
                .where(qMenu.level.eq(1))
                .fetch()
                .stream()
                .map(item -> new SelectRoleVO().setId(item.get(qMenu.id)).setName(item.get(qMenu.name)))
                .collect(Collectors.toList());
    }

    @ReadOnly
    @Override
    public List<TreeMenuVO> allMenuList() {
        return menuDao.findAllByLevelOrderByIdDesc(1)
                .stream()
                .map(
                        menu -> new TreeMenuVO()
                                .setId(menu.getId())
                                .setName(menu.getName())
                                .setChildren(getChildren(menu))
                ).collect(Collectors.toList());
    }

    private List<TreeMenuVO> getChildren(MenuPO menu) {
        if (menu.getChildren() != null && menu.getChildren().size() > 0) {
            List<TreeMenuVO> list = new ArrayList<>(menu.getChildren().size());
            menu.getChildren().forEach(children -> list.add(new TreeMenuVO()
                    .setId(children.getId())
                    .setName(children.getName())
                    .setChildren(getChildren(children))));

            return list;
        }
        return null;
    }
}
