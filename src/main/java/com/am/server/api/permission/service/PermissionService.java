package com.am.server.api.permission.service;

/**
 * @author 阮雪峰
 * @date 2019年9月18日16:47:58
 */
public interface PermissionService {
    /**
     * 将权限加载至缓存
     */
    void loadPermissionToCache();
}
