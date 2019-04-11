package com.am.server.api.admin.user.interceptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  权限
 * @author 阮雪峰
 * @date 2018/8/2 13:01
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {
    String value() default "";

    /**
     * 在注解只加在类上时，权限判断时会默认类里所有的方法都需要访问权限，
     * 在check设置为false时则不进行权限校验，为所有角色可访问
     */
    boolean check() default true;
}
