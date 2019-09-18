package com.am.server.api.menu.service.impl;

import com.am.server.api.menu.dao.rdb.MenuDAO;
import com.am.server.api.menu.pojo.ao.MenuListAO;
import com.am.server.api.menu.pojo.ao.SaveMenuAO;
import com.am.server.api.menu.pojo.ao.UpdateMenuAO;
import com.am.server.api.menu.pojo.po.MenuDO;
import com.am.server.api.menu.pojo.vo.MenuListVO;
import com.am.server.api.menu.pojo.vo.TreeMenuVO;
import com.am.server.api.menu.service.MenuService;
import com.am.server.api.role.pojo.vo.SelectRoleVO;
import com.am.server.api.user.pojo.po.AdminUserDO;
import com.am.server.api.user.service.UserPermissionCacheService;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.base.service.CommonService;
import com.am.server.common.util.IdUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    private final MenuDAO menuDAO;

    private final UserPermissionCacheService userPermissionCacheService;

    private final CommonService commonService;

    public MenuServiceImpl(MenuDAO menuDAO, UserPermissionCacheService userPermissionCacheService, CommonService commonService) {
        this.menuDAO = menuDAO;
        this.userPermissionCacheService = userPermissionCacheService;
        this.commonService = commonService;
    }

    @ReadOnly
    @Override
    public PageVO<MenuListVO> list(MenuListAO menuListAo) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                .withIgnoreNullValues()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("mark", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<MenuDO> example = Example.of(new MenuDO().setName(menuListAo.getName()).setMark(menuListAo.getMark()), matcher);

        Page<MenuDO> page = menuDAO.findAll(example, PageRequest.of(menuListAo.getPage() - 1, menuListAo.getPageSize()));
        return new PageVO<MenuListVO>()
                .setPage(menuListAo.getPage())
                .setPageSize(menuListAo.getPageSize())
                .setTotal(page.getNumber())
                .setRows(
                        page.getContent().stream()
                                .map(menu ->
                                        new MenuListVO().setId(menu.getId())
                                                .setName(menu.getName())
                                                .setMark(menu.getMark())
                                                .setPid(Optional.ofNullable(menu.getParent()).map(MenuDO::getId).orElse(null))
                                                .setParentName(Optional.ofNullable(menu.getParent()).map(MenuDO::getName).orElse(""))
                                                .setCreatedBy(Optional.ofNullable(menu.getCreatedBy()).map(AdminUserDO::getName).orElse(""))
                                                .setCreateTime(menu.getCreatedTime())
                                ).collect(Collectors.toList())
                );
    }

    @Commit
    @Override
    public void save(SaveMenuAO menu) {
        MenuDO menuDO = new MenuDO()
                .setId(IdUtils.getId())
                .setName(menu.getName())
                .setMark(menu.getMark())
                .setCreatedBy(new AdminUserDO().setId(commonService.getLoginUserId()))
                .setCreatedTime(LocalDateTime.now())
                .setParent(Optional.ofNullable(menu.getPid()).map(pid->new MenuDO().setId(pid)).orElse(null))
                .setLevel(menu.getPid() == null ? 1 : 2);

        menuDAO.save(menuDO);
    }

    @Commit
    @Override
    public void update(UpdateMenuAO menu) {
        menuDAO.findById(menu.getId())
                .ifPresent(menuDO -> {
                    menuDO.setName(menu.getName())
                            .setMark(menu.getMark())
                            .setParent(Optional.ofNullable(menu.getPid()).map(pid->new MenuDO().setId(pid)).orElse(null))
                            .setLevel(menu.getPid() == null ? 1 : 2);
                    menuDAO.save(menuDO);
                });

    }

    @Commit
    @Override
    public void delete(Long id) {
        menuDAO.deleteById(id);
        menuDAO.deleteRelateRoles(id);
        userPermissionCacheService.removeAll();
    }

    @ReadOnly
    @Override
    public List<SelectRoleVO> parentList() {
        return menuDAO.findAllByLevelOrderByIdDesc(1).stream()
                .map(menu ->
                        new SelectRoleVO().setId(menu.getId())
                                .setName(menu.getName())
                ).collect(Collectors.toList());
    }

    @ReadOnly
    @Override
    public List<TreeMenuVO> allMenuList() {
        return menuDAO.findAllByLevelOrderByIdDesc(1)
                .stream()
                .map(
                        menu -> new TreeMenuVO()
                                .setId(menu.getId())
                                .setName(menu.getName())
                                .setChildren(getChildren(menu))
                ).collect(Collectors.toList());
    }

    private List<TreeMenuVO> getChildren(MenuDO menu) {
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
