package com.am.server.api.user.dao.cache;

import com.am.server.api.user.pojo.po.UserPermissionDO;

import java.util.Set;

/**
 * @author 阮雪峰
 * @date 2019年9月5日22:25:08
 */

public interface UserPermissionCacheDAO {

    /**
     * 查询用户权限缓存
     *
     * @param uid 用户主键
     * @return UserPermissionDO
     */
    UserPermissionDO findById(Long uid);

    /**
     * 保存用户权限到缓存中
     *
     * @param uid         用户主键
     * @param permissions 权限集合
     */
    void save(Long uid, Set<String> permissions);

    /**
     * 删除用户权限缓存
     *
     * @param uid uid
     */
    void remove(Long uid);

    /**
     * 清空所有用户权限缓存
     */
    void removeAll();

    /**
     * 清空所有用户权限缓存
     * @param ids ids
     */
    void removeAll(Long... ids);
}
