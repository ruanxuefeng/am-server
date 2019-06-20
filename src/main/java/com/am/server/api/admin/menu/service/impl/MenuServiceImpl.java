package com.am.server.api.admin.menu.service.impl;

import com.am.server.api.admin.menu.dao.jpa.MenuDao;
import com.am.server.api.admin.menu.entity.Menu;
import com.am.server.api.admin.menu.entity.QMenu;
import com.am.server.api.admin.menu.service.MenuService;
import com.am.server.advice.update.annotation.Save;
import com.am.server.api.admin.user.pojo.QAdminUser;
import com.am.server.api.admin.user.service.UserPermissionCacheService;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.entity.PageVO;
import com.am.server.common.constant.Constant;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author 阮雪峰
 * @date 2018/7/30 15:17
 */
@Service("menuService")
public class MenuServiceImpl implements MenuService {

    @Resource(name = "menuDao")
    private MenuDao menuDao;

    private final UserPermissionCacheService userPermissionCacheService;

    @PersistenceContext
    private EntityManager entityManager;

    public MenuServiceImpl(UserPermissionCacheService userPermissionCacheService) {
        this.userPermissionCacheService = userPermissionCacheService;
    }

    @ReadOnly
    @Override
    public void list(PageVO<Menu> page, Menu menu) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QMenu qMenu = QMenu.menu;
        QMenu qParent = new QMenu("parent");
        QAdminUser qAdminUser = QAdminUser.adminUser;

        JPAQuery<Menu> query = queryFactory.select(
                Projections.bean(
                        Menu.class,
                        qMenu.id,
                        qMenu.name,
                        qMenu.key,
                        qMenu.createTime,
                        qMenu.pid,
                        qAdminUser.name.as(Constant.CREATOR_NAME),
                        qParent.name.as("parentName")
                )
        )
                .from(qMenu)
                .leftJoin(qAdminUser).on(qMenu.creator.eq(qAdminUser.id))
                .leftJoin(qParent).on(qMenu.pid.eq(qParent.id))
                .offset(page.getCol())
                .limit(page.getPageSize())
                .orderBy(qMenu.id.desc());

        Optional<Menu> menuOptional = Optional.of(menu);

        menuOptional.map(Menu::getName)
                .filter(name -> !name.isEmpty())
                .ifPresent(name -> query.where(qMenu.name.like("%" + name + "%")));

        menuOptional.map(Menu::getKey)
                .filter(key -> !key.isEmpty())
                .ifPresent(key -> query.where(qMenu.key.like("%" + key + "%")));


        page.setTotal((int) query.fetchCount());
        page.setRows(query.fetch());

    }

    @Save
    @Commit
    @Override
    public void save(Menu menu) {
        setLevel(menu);
        menuDao.save(menu);
    }

    @ReadOnly
    @Override
    public Menu detail(Menu menu) {
        return menuDao.findById(menu.getId()).orElse(null);
    }

    @Commit
    @Override
    public void update(Menu menu) {
        setLevel(menu);
        menuDao.save(menu);
    }

    private void setLevel(Menu menu) {
        if (menu.getPid() == null) {
            menu.setLevel(1);
        } else {
            menu.setLevel(2);
        }
    }

    @Commit
    @Override
    public void delete(Menu menu) {
        menuDao.delete(menu);
        menuDao.deleteRelateRoles(menu.getId());
        userPermissionCacheService.removeAll();
    }

    @ReadOnly
    @Override
    public List<Menu> parentList() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QMenu qMenu = QMenu.menu;
        return queryFactory.select(qMenu.id, qMenu.name)
                .from(qMenu)
                .where(qMenu.level.eq(1))
                .fetch()
                .stream()
                .map(item -> new Menu(item.get(qMenu.id), item.get(qMenu.name)))
                .collect(Collectors.toList());
    }

    @ReadOnly
    @Override
    public List<Menu> menuList() {
        return menuDao.findAllByLevelOrderByIdDesc(1);

    }
}
