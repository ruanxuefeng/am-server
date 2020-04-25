package com.am.server.api.permission.dao.cache.impl;

import com.am.server.api.permission.dao.cache.PermissionDao;
import com.am.server.api.permission.pojo.po.PermissionTreeDo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.TreeSet;

/**
 * @author 阮雪峰
 * @date 2019年10月14日13:23:01
 */
@Repository
public class PermissionDaoImpl implements PermissionDao {

    private final static String PERMISSION_TREE_KEY = "permission: tree";

    private final RedisTemplate<String, TreeSet<PermissionTreeDo>> permissionCacheRedisTemplate;

    public PermissionDaoImpl(RedisTemplate<String, TreeSet<PermissionTreeDo>> permissionCacheRedisTemplate) {
        this.permissionCacheRedisTemplate = permissionCacheRedisTemplate;
    }


    @Override
    public void save(TreeSet<PermissionTreeDo> permissionTree) {
        permissionCacheRedisTemplate.opsForValue().set(PERMISSION_TREE_KEY, permissionTree);
    }

    @Override
    public TreeSet<PermissionTreeDo> findAll() {
        return permissionCacheRedisTemplate.opsForValue().get(PERMISSION_TREE_KEY);
    }
}
