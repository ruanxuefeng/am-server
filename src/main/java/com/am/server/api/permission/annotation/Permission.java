package com.am.server.api.permission.annotation;

import java.lang.annotation.*;

/**
 * 权限,需要具体到每个controller及方法
 *
 * @author 阮雪峰
 * @date 2018/8/2 13:01
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {


    /**
     * 权限分类
     *
     * @return Menu[]
     */
    Menu[] menus() default {};

    /**
     * 唯一标识，用于权限判断
     *
     * @return value
     */
    String value() default "";

    /**
     * 中文名字
     *
     * @return name
     */
    String name() default "";

    int sort() default 0;

    /**
     * 在注解只加在类上时，权限判断时会默认类里所有的方法都需要访问权限，
     * 在check设置为false时则不进行权限校验，为所有角色可访问
     */
    boolean check() default true;
}
