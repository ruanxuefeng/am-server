package com.am.server.advice.update.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  修改的时候赋值修改时间
 * @author 阮雪峰
 * @date 2018/9/5 13:22
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Update {
}
