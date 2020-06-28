package com.am.server.api.permission.dao.cache.impl;

import com.am.server.api.permission.dao.cache.PermissionDao;
import com.am.server.api.permission.pojo.po.PermissionCollection;
import com.am.server.api.permission.pojo.po.PermissionTreeDo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author 阮雪峰
 * @date 2019年10月14日13:23:01
 */
@Repository
public class PermissionDaoImpl implements PermissionDao {

    private final static String PERMISSION_TREE_KEY = "permission: tree";

    private final RedisTemplate<String, PermissionCollection> permissionCacheRedisTemplate;

    public PermissionDaoImpl(RedisTemplate<String, PermissionCollection> permissionCacheRedisTemplate) {
        this.permissionCacheRedisTemplate = permissionCacheRedisTemplate;
    }


    @Override
    public void save(PermissionCollection collection) {
        permissionCacheRedisTemplate.opsForValue().set(PERMISSION_TREE_KEY, collection);
    }

    @Override
    public TreeSet<PermissionTreeDo> findAll() {
        return Optional.ofNullable(permissionCacheRedisTemplate.opsForValue().get(PERMISSION_TREE_KEY))
                .map(PermissionCollection::getSet)
                .orElse(new TreeSet<>());
    }

    @Override
    public List<String> findAllList() {
        return Optional.ofNullable(permissionCacheRedisTemplate.opsForValue().get(PERMISSION_TREE_KEY))
                .map(PermissionCollection::getPermissionList)
                .orElseGet(ArrayList::new);
    }
}
