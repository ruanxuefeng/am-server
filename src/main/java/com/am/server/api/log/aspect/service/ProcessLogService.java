package com.am.server.api.log.aspect.service;

import com.am.server.api.log.aspect.annotation.WriteLog;

import java.lang.reflect.Method;

/**
 * 处理日志用户操作日志接口
 *
 * @author 阮雪峰
 * @date 2019/3/27 14:33
 */
public interface ProcessLogService {
    /**
     * 处理日志
     * @param targetClass 需要记录日志的类
     * @param targetClassWriteLog 需要记录日志类的WriteLog注解
     * @param targetMethod 需要记录日志类具体执行的方法
     * @param targetMethodWriteLog 方法上的WriteLog注解
     * @date 2019/3/27 14:45
     * @author 阮雪峰
     */
    void process(Class<?> targetClass, WriteLog targetClassWriteLog, Method targetMethod, WriteLog targetMethodWriteLog);
}
