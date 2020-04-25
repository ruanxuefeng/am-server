package com.am.server.api.user.service.impl;

import com.am.server.api.role.pojo.po.RoleDo;
import com.am.server.api.user.dao.cache.UserPermissionCacheDao;
import com.am.server.api.user.dao.rdb.AdminUserDao;
import com.am.server.api.user.pojo.po.AdminUserDo;
import com.am.server.api.user.pojo.po.UserPermissionDo;
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

    private final UserPermissionCacheDao userPermissionCacheDAO;

    private final AdminUserDao adminUserDao;

    public RedisUserPermissionCacheServiceImpl(UserPermissionCacheDao userPermissionCacheDAO, AdminUserDao adminUserDao) {
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
                .map(UserPermissionDo::getPermissions)
                .orElseGet(() -> {
                    Set<String> permissions = new HashSet<>();
                    List<RoleDo> roleList = adminUserDao.findById(uid)
                            .map(AdminUserDo::getRoles)
                            .orElse(new ArrayList<>());
                    roleList.stream()
                            .map(RoleDo::getPermissions)
                            .forEach(permissions::addAll);
                    if (permissions.size() == 0) {
                        return null;
                    }
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
