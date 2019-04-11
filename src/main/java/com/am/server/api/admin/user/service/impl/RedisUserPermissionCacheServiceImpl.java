package com.am.server.api.admin.user.service.impl;

import com.am.server.api.admin.user.dao.jpa.AdminUserDao;
import com.am.server.api.admin.user.service.UserPermissionCacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * redis用户权限缓存实现
 *
 * @author 阮雪峰
 * @date 2019/3/27 13:18
 */
public class RedisUserPermissionCacheServiceImpl implements UserPermissionCacheService {

    private static final String PREFIX_KEY = "user-";
    @Resource
    private RedisTemplate<String, List<String>> redisTemplate;

    @Resource
    private AdminUserDao adminUserDao;

    @Override
    public void set(Long uid, List<String> permissions) {
        redisTemplate.opsForValue().set(PREFIX_KEY + uid, permissions);
    }

    @Override
    public List<String> get(Long uid) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(uid))
                .orElseGet(() -> {
                    List<String> list = adminUserDao.findMenuList(uid);
                    redisTemplate.opsForValue().set(PREFIX_KEY + uid, list);
                    return list;
                });
    }

    @Override
    public void remove(Long uid) {
        redisTemplate.delete(PREFIX_KEY + uid);
    }

    @Override
    public void removeAll() {
        Set<String> keys = redisTemplate.keys(PREFIX_KEY);
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }
}
