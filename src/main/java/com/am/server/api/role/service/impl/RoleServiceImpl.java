package com.am.server.api.role.service.impl;

import com.am.server.api.menu.pojo.po.MenuDO;
import com.am.server.api.role.dao.rdb.RoleDAO;
import com.am.server.api.role.pojo.ao.RoleListAO;
import com.am.server.api.role.pojo.ao.SaveRoleAO;
import com.am.server.api.role.pojo.ao.UpdateRoleAO;
import com.am.server.api.role.pojo.ao.UpdateRoleMenuAO;
import com.am.server.api.role.pojo.po.RoleDO;
import com.am.server.api.role.pojo.vo.RoleListVo;
import com.am.server.api.role.pojo.vo.SelectRoleVO;
import com.am.server.api.role.service.RoleService;
import com.am.server.api.user.pojo.po.AdminUserDO;
import com.am.server.api.user.service.UserPermissionCacheService;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.base.service.CommonService;
import com.am.server.common.util.IdUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 阮雪峰
 * @date 2018/7/27 10:47
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {

    private final RoleDAO roleDAO;


    private final UserPermissionCacheService userPermissionCacheService;

    private final CommonService commonService;

    public RoleServiceImpl(RoleDAO roleDAO, UserPermissionCacheService userPermissionCacheService, CommonService commonService) {
        this.roleDAO = roleDAO;
        this.userPermissionCacheService = userPermissionCacheService;
        this.commonService = commonService;
    }

    @ReadOnly
    @Override
    public PageVO<RoleListVo> list(RoleListAO roleListAo) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<RoleDO> example = Example.of(new RoleDO().setName(roleListAo.getName()), matcher);

        Page<RoleDO> page = roleDAO.findAll(example, PageRequest.of(roleListAo.getPage() - 1, roleListAo.getPageSize()));
        return new PageVO<RoleListVo>()
                .setPage(roleListAo.getPage())
                .setPageSize(roleListAo.getPageSize())
                .setTotal(page.getNumber())
                .setRows(
                        page.getContent()
                                .stream().map(role ->
                                new RoleListVo().setId(role.getId())
                                        .setName(role.getName())
                                        .setCreateTime(role.getCreatedTime())
                                        .setCreatorName(Optional.ofNullable(role.getCreatedBy()).map(AdminUserDO::getUsername).orElse(""))
                                        .setMemo(role.getMemo())
                        ).collect(Collectors.toList())
                );
    }

    @Commit
    @Override
    public void save(SaveRoleAO roleAo) {
        roleDAO.save(
                new RoleDO()
                        .setId(IdUtils.getId())
                        .setName(roleAo.getName())
                        .setMemo(roleAo.getMemo())
                        .setCreatedTime(LocalDateTime.now())
                        .setCreatedBy(new AdminUserDO().setId(commonService.getLoginUserId()))
        );
    }

    @Commit
    @Override
    public void update(UpdateRoleAO roleAo) {
        roleDAO.findById(roleAo.getId())
                .ifPresent(role -> roleDAO.save(
                        role.setName(roleAo.getName())
                                .setMemo(roleAo.getMemo())
                ));
    }

    @Commit
    @Override
    public void delete(Long id) {
        roleDAO.deleteById(id);
        userPermissionCacheService.removeAll();
    }

    @ReadOnly
    @Override
    public List<SelectRoleVO> findAll() {
        return roleDAO.findAll(Sort.by(Sort.Order.desc("id")))
                .stream()
                .map(role -> new SelectRoleVO().setId(role.getId()).setName(role.getName()))
                .collect(Collectors.toList());
    }

    @Commit
    @Override
    public void updateMenuList(UpdateRoleMenuAO updateRoleMenuAo) {
        roleDAO.findById(updateRoleMenuAo.getId())
                .ifPresent(role -> {
                    ArrayList<MenuDO> newMenuList = new ArrayList<>(8);
                    Optional.ofNullable(updateRoleMenuAo.getMenuList())
                            .filter(menuList -> !menuList.isEmpty())
                            .ifPresent(menuList -> menuList.forEach(menu -> newMenuList.add(new MenuDO().setId(menu))));
                    role.setMenus(newMenuList);
                    roleDAO.save(role);
                });
    }

    @Override
    public List<Long> getMenuList(Long id) {
        List<MenuDO> menuList = roleDAO.findById(id)
                .map(RoleDO::getMenus)
                .orElse(new ArrayList<>());
        return menuList.stream()
                .map(MenuDO::getId)
                .collect(Collectors.toList());
    }
}