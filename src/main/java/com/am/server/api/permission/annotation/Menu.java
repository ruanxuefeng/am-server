package com.am.server.api.permission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
 * @author 阮雪峰
 * @date 2019年10月14日13:22:49
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
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

    int sort() default 0;
}
