package com.am.server.api.user.dao.cache.impl;

import com.am.server.api.user.dao.cache.UserPermissionCacheDao;
import com.am.server.api.user.pojo.po.UserPermissionDo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;


/**
 * @author 阮雪峰
 * @date 2019年9月5日22:27:02
 */
@Repository
public class UserPermissionCacheDaoImpl implements UserPermissionCacheDao {
    private static final String KEY = "user: permission: ";
    /**
     * 权限缓存过期时间为2小时
     */
    private static final int PERMISSION_CACHE_HOUR = 2;

    private final RedisTemplate<String, UserPermissionDo> userPermissionCacheRedisTemplate;

    public UserPermissionCacheDaoImpl(RedisTemplate<String, UserPermissionDo> userPermissionCacheRedisTemplate) {
        this.userPermissionCacheRedisTemplate = userPermissionCacheRedisTemplate;
    }

    @Override
    public UserPermissionDo findById(Long uid) {
        return userPermissionCacheRedisTemplate.opsForValue().get(KEY + uid);
    }

    @Override
    public void save(Long uid, Set<String> permissions) {
        userPermissionCacheRedisTemplate.opsForValue()
                .set(
                        KEY + uid,
                        new UserPermissionDo().setId(uid).setPermissions(permissions),
                        Duration.ofHours(PERMISSION_CACHE_HOUR)
                );
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

    @Override
    public void removeAll(Long... ids) {
        Set<String> keys = new HashSet<>();
        for (Long id : ids) {
            keys.add(KEY + id);
        }
        userPermissionCacheRedisTemplate.delete(keys);
    }
}
