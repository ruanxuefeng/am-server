package com.am.server.api.permission.dao.cache;

import com.am.server.api.permission.pojo.po.PermissionTreeDo;

import java.util.TreeSet;

/**
 * @author 阮雪峰
 */
public interface PermissionDao {
    /**
     * 将权限树存入缓存
     *
     * @param permissionTree permissionTree
     */
    void save(TreeSet<PermissionTreeDo> permissionTree);

    /**
     * 所有权限
     * @return List<PermissionTreeVO>
     */
    TreeSet<PermissionTreeDo> findAll();
}
