package com.am.server.api.user.service;

import java.util.Optional;
import java.util.Set;

/**
 *  用户权限缓存
 * @author 阮雪峰
 * @date 2018/8/2 12:53
 */
public interface UserPermissionCacheService {
    /**
     * 用户是否有访问权限
     * @param uid 登录用户id
     * @param permission 待验证的权限
     * @return boolean
     * @author 阮雪峰
     * @date 2018/8/2 13:49
     */
    default boolean hasPermission(Long uid, String permission){
        //判断用户是否有权限
        return Optional.ofNullable(get(uid))
                .filter(permissions -> permissions.contains(permission))
                .isPresent();
    }

    /**
     * 将用户权限放入缓存中
     * @param uid 用户主键
     * @param permissions 用户权限
     * @author 阮雪峰
     * @date 2018/8/24 14:50
     */
    void set(Long uid, Set<String> permissions);

    /**
     * 在缓存中获取用户权限
     * @param uid 用户主键
     * @return void
     * @author 阮雪峰
     * @date 2018/8/24 14:44
     */
    Set<String> get(Long uid);

    /**
     * 删除缓存
     * @param uid uid
     * @date 2019/4/9 15:08
     * @author 阮雪峰
     */
    void remove(Long uid);

    /**
     * 清空权限缓存
     * @author 阮雪峰
     * @date 2019年6月26日14:32:58
     */
    void removeAll();

    /**
     * 清除用户权限缓存
     * @param ids 需要清除用户的主键
     */
    void removeAll(Long ...ids);
}
