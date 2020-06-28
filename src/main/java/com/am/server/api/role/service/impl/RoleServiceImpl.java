package com.am.server.api.role.service.impl;

import com.am.server.api.role.dao.rdb.RoleDao;
import com.am.server.api.role.pojo.ao.RoleListAo;
import com.am.server.api.role.pojo.ao.SaveRoleAo;
import com.am.server.api.role.pojo.ao.UpdateRoleAo;
import com.am.server.api.role.pojo.ao.UpdateRolePermissionAo;
import com.am.server.api.role.pojo.po.RoleDo;
import com.am.server.api.role.pojo.vo.RoleListVo;
import com.am.server.api.role.pojo.vo.SelectRoleVo;
import com.am.server.api.role.service.RoleService;
import com.am.server.api.user.pojo.po.AdminUserDo;
import com.am.server.api.user.service.UserPermissionCacheService;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.pojo.vo.PageVO;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 阮雪峰
 * @date 2018/7/27 10:47
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    private final UserPermissionCacheService userPermissionCacheService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public RoleServiceImpl(RoleDao roleDao, UserPermissionCacheService userPermissionCacheService, SimpMessagingTemplate simpMessagingTemplate) {
        this.roleDao = roleDao;
        this.userPermissionCacheService = userPermissionCacheService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @ReadOnly
    @Override
    public PageVO<RoleListVo> list(RoleListAo roleListAo) {
        Page<RoleDo> page = roleDao.findAll(roleListAo);
        return new PageVO<RoleListVo>()
                .setPage(roleListAo.getPage())
                .setPageSize(roleListAo.getPageSize())
                .setTotal((int) page.getTotalElements())
                .setTotalPage(page.getTotalPages())
                .setRows(
                        page.getContent()
                                .stream().map(role ->
                                new RoleListVo().setId(role.getId())
                                        .setName(role.getName())
                                        .setCreateTime(role.getCreatedTime())
                                        .setCreatorName(Optional.ofNullable(role.getCreatedBy()).map(AdminUserDo::getUsername).orElse(""))
                                        .setMemo(role.getMemo())
                        ).collect(Collectors.toList())
                );
    }

    @Commit
    @Override
    public void save(SaveRoleAo roleAo) {
        RoleDo role = new RoleDo()
                .setName(roleAo.getName())
                .setMemo(roleAo.getMemo());

        roleDao.save(role);
    }

    @Commit
    @Override
    public void update(UpdateRoleAo roleAo) {
        roleDao.findById(roleAo.getId())
                .ifPresent(role -> {
                    role.setName(roleAo.getName()).setMemo(roleAo.getMemo());

                    roleDao.save(role);
                });
    }

    @Commit
    @Override
    public void delete(Long id) {
        roleDao.deleteById(id);
        userPermissionCacheService.removeAll();
    }

    @ReadOnly
    @Override
    public List<SelectRoleVo> findAll() {
        return roleDao.findAllOrderByIdDesc()
                .stream()
                .map(role -> new SelectRoleVo().setId(role.getId()).setName(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void updatePermissions(UpdateRolePermissionAo updateRolePermissionAo) {
        roleDao.findById(updateRolePermissionAo.getId())
                .ifPresent(role -> {
                    List<Long> userIds = new ArrayList<>();
                    for (AdminUserDo user : role.getUsers()) {
                        userIds.add(user.getId());
                        simpMessagingTemplate.convertAndSend("/topic/permission/" + user.getId(), true);
                    }
                    userPermissionCacheService.removeAll(userIds.toArray(new Long[0]));
                    roleDao.save(role.setPermissions(updateRolePermissionAo.getPermissions()));
                });
    }

    @Override
    public List<String> findPermissions(Long id) {
        return roleDao.findById(id)
                .map(RoleDo::getPermissions)
                .orElse(new ArrayList<>());
    }
}
