package com.am.server.api.permission.dao.cache;

import com.am.server.api.permission.pojo.po.PermissionTreeDO;

import java.util.TreeSet;

/**
 * @author 阮雪峰
 */
public interface PermissionDAO {
    /**
     * 将权限树存入缓存
     *
     * @param permissionTree permissionTree
     */
    void save(TreeSet<PermissionTreeDO> permissionTree);

    /**
     * 所有权限
     * @return List<PermissionTreeVO>
     */
    TreeSet<PermissionTreeDO> findAll();
}
