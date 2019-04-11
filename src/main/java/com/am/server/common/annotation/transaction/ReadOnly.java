package com.am.server.common.annotation.transaction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 只读事务
 * @author 阮雪峰
 * @date 2019/1/18 12:55
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(propagation= Propagation.NOT_SUPPORTED,readOnly=true)
public @interface ReadOnly {

}
