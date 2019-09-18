package com.am.server.api.permission.interceptor.annotation;

/**
 * 具体权限的集合
 * 例如：
 * 系统管理（Menu）
 *   用户管理（permission）
 *     列表（permission）
 *     新增（permission）
 *     修改（permission）
 *   角色管理（permission）
 *     列表（permission）
 *     新增（permission）
 *     修改（permission）
 */
public @interface Menu {
    /**
     * 唯一标识
     *
     * @return value
     */
    String value();

    /**
     * 中文名字
     *
     * @return name
     */
    String name();
}
