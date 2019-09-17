package com.am.server.api.user.dao.cache.impl;

import com.am.server.api.user.dao.cache.UserPermissionCacheDAO;
import com.am.server.api.user.pojo.po.UserPermissionDO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.Set;


/**
 * @author 阮雪峰
 * @date 2019年9月5日22:27:02
 */
@Repository
public class UserPermissionCacheDAOImpl implements UserPermissionCacheDAO {
    private static final String KEY = "user: permission ";
    /**
     * 权限缓存过期时间为2小时
     */
    private static final int PERMISSION_CACHE_HOUR = 2;

    private final RedisTemplate<String, UserPermissionDO> userPermissionCacheRedisTemplate;

    public UserPermissionCacheDAOImpl(RedisTemplate<String, UserPermissionDO> userPermissionCacheRedisTemplate) {
        this.userPermissionCacheRedisTemplate = userPermissionCacheRedisTemplate;
    }

    @Override
    public UserPermissionDO findById(Long uid) {
        return userPermissionCacheRedisTemplate.opsForValue().get(KEY + uid);
    }

    @Override
    public void save(Long uid, Set<String> permissions) {
        userPermissionCacheRedisTemplate.opsForValue().set(KEY + uid, new UserPermissionDO().setId(uid).setPermissions(permissions), Duration.ofHours(PERMISSION_CACHE_HOUR));
    }

    @Override
    public void remove(Long uid) {
        userPermissionCacheRedisTemplate.delete(KEY + uid);
    }

    @Override
    public void removeAll() {
        Set<String> keys = userPermissionCacheRedisTemplate.keys(KEY + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            userPermissionCacheRedisTemplate.delete(keys);
        }
    }
}
