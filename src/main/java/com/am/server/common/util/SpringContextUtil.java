package com.am.server.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author ruanxuefeng
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @param name bean name
     * @param clz  bean type
     * @param <T>  bean type
     * @return T
     * @throws BeansException BeansException
     */
    public static <T> T getBean(String name, Class<T> clz) throws BeansException {
        return applicationContext.getBean(name, clz);
    }

    /**
     *
     * @param clz bean type
     * @param <T> bean type
     * @return T
     * @throws BeansException BeansException
     */
    public static <T> T getBean(Class<T> clz) throws BeansException {
        return applicationContext.getBean(clz);
    }
}
