package com.am.server.api.permission.dao.cache.impl;

import com.am.server.api.permission.dao.cache.PermissionDAO;
import com.am.server.api.permission.pojo.po.PermissionTreeDO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.TreeSet;

@Repository
public class PermissionDAOImpl implements PermissionDAO {

    private final static String PERMISSION_TREE_KEY = "permission: tree";

    private final RedisTemplate<String, TreeSet<PermissionTreeDO>> permissionCacheRedisTemplate;

    public PermissionDAOImpl(RedisTemplate<String, TreeSet<PermissionTreeDO>> permissionCacheRedisTemplate) {
        this.permissionCacheRedisTemplate = permissionCacheRedisTemplate;
    }


    @Override
    public void save(TreeSet<PermissionTreeDO> permissionTree) {
        permissionCacheRedisTemplate.opsForValue().set(PERMISSION_TREE_KEY, permissionTree);
    }

    @Override
    public TreeSet<PermissionTreeDO> findAll() {
        return permissionCacheRedisTemplate.opsForValue().get(PERMISSION_TREE_KEY);
    }
}
