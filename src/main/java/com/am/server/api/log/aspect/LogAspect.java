package com.am.server.api.log.aspect;

import com.am.server.api.log.aspect.annotation.WriteLog;
import com.am.server.api.log.aspect.service.ProcessLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 *
 * @author 阮雪峰
 * @date 2018/8/1 13:52
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    private final ApplicationContext applicationContext;

    public LogAspect(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @After("@annotation(com.am.server.api.log.aspect.annotation.WriteLog)")
    public void doAfter(JoinPoint joinPoint) {

        Class<?> target = joinPoint.getTarget().getClass();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method method;
        try {
            method = target.getMethod(signature.getName(), signature.getParameterTypes());

            WriteLog targetAnnotation = target.getAnnotation(WriteLog.class);
            WriteLog methodAnnotation = method.getAnnotation(WriteLog.class);

            ProcessLogService processLogService = applicationContext.getBean(methodAnnotation.processClass());
            processLogService.process(target, targetAnnotation, method, methodAnnotation);

        } catch (NoSuchMethodException e) {
            log.error("方法未找到，类：{}， 方法：{}", target.getName(), signature.getName());
        }

    }
}
