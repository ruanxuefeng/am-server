package com.am.server.api.admin.log.aspect.annotation;

import com.am.server.api.admin.log.aspect.service.ProcessLogService;
import com.am.server.api.admin.log.aspect.service.impl.DefaultProcessLogServiceImpl;

import java.lang.annotation.*;

/**
 * 需要记录日志
 *
 * @author 阮雪峰
 * @date 2018/8/1 13:53
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WriteLog{
    String value();

    Class<? extends ProcessLogService> processClass() default DefaultProcessLogServiceImpl.class;
}
