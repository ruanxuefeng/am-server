package com.am.server.api.user.service.impl;

import com.am.server.api.role.pojo.po.RoleDO;
import com.am.server.api.user.dao.cache.UserPermissionCacheDAO;
import com.am.server.api.user.dao.rdb.AdminUserDAO;
import com.am.server.api.user.pojo.po.AdminUserDO;
import com.am.server.api.user.pojo.po.UserPermissionDO;
import com.am.server.api.user.service.UserPermissionCacheService;
import com.am.server.common.annotation.transaction.ReadOnly;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * redis用户权限缓存实现
 *
 * @author 阮雪峰
 * @date 2019/3/27 13:18
 */
@Service
public class RedisUserPermissionCacheServiceImpl implements UserPermissionCacheService {

    private final UserPermissionCacheDAO userPermissionCacheDAO;

    private final AdminUserDAO adminUserDao;

    public RedisUserPermissionCacheServiceImpl(UserPermissionCacheDAO userPermissionCacheDAO, AdminUserDAO adminUserDao) {
        this.userPermissionCacheDAO = userPermissionCacheDAO;
        this.adminUserDao = adminUserDao;
    }

    @Override
    public void set(Long uid, Set<String> permissions) {
        userPermissionCacheDAO.save(uid, permissions);
    }

    @ReadOnly
    @Override
    public Set<String> get(Long uid) {
        return Optional.ofNullable(userPermissionCacheDAO.findById(uid))
                .map(UserPermissionDO::getPermissions)
                .orElseGet(() -> {
                    Set<String> permissions = new HashSet<>();
                    List<RoleDO> roleList = adminUserDao.findById(uid)
                            .map(AdminUserDO::getRoles)
                            .orElse(new ArrayList<>());
                    roleList.stream()
                            .map(RoleDO::getPermissions)
                            .forEach(permissions::addAll);
                    userPermissionCacheDAO.save(uid, permissions);
                    return permissions;
                });
    }

    @Override
    public void remove(Long uid) {
        userPermissionCacheDAO.remove(uid);
    }

    @Override
    public void removeAll() {
        userPermissionCacheDAO.removeAll();
    }

    @Override
    public void removeAll(Long... ids) {
        userPermissionCacheDAO.removeAll(ids);
    }
}
