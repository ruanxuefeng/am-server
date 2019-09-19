package com.am.server.api.permission.service;

import com.am.server.api.role.pojo.vo.PermissionTreeVO;

import java.util.List;

/**
 * @author 阮雪峰
 * @date 2019年9月18日16:47:58
 */
public interface PermissionService {
    /**
     * 将权限加载至缓存
     */
    void loadPermissionToCache();

    /**
     * 查询所有的权限
     * @return List<PermissionTreeVO>
     */
    List<PermissionTreeVO> findAll();
}
