package com.am.server.api.admin.user.service.impl;

import com.am.server.api.admin.user.dao.jpa.AdminUserDao;
import com.am.server.api.admin.user.dao.mongo.UserPermissionCacheDao;
import com.am.server.api.admin.user.entity.UserPermissionCache;
import com.am.server.api.admin.user.service.UserPermissionCacheService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author 阮雪峰
 * @date 2018/8/2 13:49
 */
public class MongoUserPermissionCacheServiceImpl implements UserPermissionCacheService {

    private final UserPermissionCacheDao userPermissionCacheDao;

    public MongoUserPermissionCacheServiceImpl(UserPermissionCacheDao userPermissionCacheDao) {
        this.userPermissionCacheDao = userPermissionCacheDao;
    }

    @Resource
    private AdminUserDao adminUserDao;

    @Override
    public void set(Long uid, List<String> permissions) {
        userPermissionCacheDao.save(
                UserPermissionCache.builder()
                        .id(uid)
                        .permissions(permissions)
                        .build()
        );
    }

    @Override
    public List<String> get(Long uid) {
        UserPermissionCache userPermissionCache = userPermissionCacheDao.findById(uid)
                .orElseGet(() -> {

                    //缓存失效重新获取
                    List<String> menuList = adminUserDao.findMenuList(uid);
                    set(uid, menuList);
                    return UserPermissionCache.builder()
                            .id(uid)
                            .permissions(menuList)
                            .build();
                });

        return Optional.ofNullable(userPermissionCache)
                .map(UserPermissionCache::getPermissions)
                .orElse(new ArrayList<>());
    }

    @Override
    public void remove(Long uid) {
        userPermissionCacheDao.deleteById(uid);
    }

    @Override
    public void removeAll() {
        userPermissionCacheDao.deleteAll();
    }
}
