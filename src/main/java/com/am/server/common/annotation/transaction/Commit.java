package com.am.server.common.annotation.transaction;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.SQLException;

/**
 * 提交事务
 * @author 阮雪峰
 * @date 2019/1/18 12:54
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(propagation= Propagation.REQUIRED,isolation= Isolation.READ_COMMITTED,rollbackFor={Exception.class, RuntimeException.class,SQLException.class})
public @interface Commit {

}
