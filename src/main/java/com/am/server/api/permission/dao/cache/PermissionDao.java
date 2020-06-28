package com.am.server.api.permission.dao.cache;

import com.am.server.api.permission.pojo.po.PermissionCollection;
import com.am.server.api.permission.pojo.po.PermissionTreeDo;

import java.util.List;
import java.util.TreeSet;

/**
 * @author 阮雪峰
 */
public interface PermissionDao {
    /**
     * 将权限树存入缓存
     *
     * @param collection collection
     */
    void save(PermissionCollection collection);

    /**
     * 所有权限，树形结构
     * @return List<PermissionTreeVO>
     */
    TreeSet<PermissionTreeDo> findAll();

    /**
     * 所有权限标识
     * @return List<String>
     */
    List<String> findAllList();
}
