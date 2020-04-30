package com.am.server.api.task.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * 定时任务工具类
 *
 * @author 阮雪峰
 * @date 2020年4月30日19:02:01
 */
public class ScheduledTaskUtils {
    private final static Map<Long, ScheduledFuture<?>> scheduledFutureMap = new ConcurrentHashMap<>();

    private ScheduledTaskUtils() {
    }

    /**
     * 添加定时任执行future
     *
     * @param id              定时任务主键
     * @param scheduledFuture scheduledFuture
     */
    public static void addScheduledTaskFuture(Long id, ScheduledFuture<?> scheduledFuture) {
        scheduledFutureMap.put(id, scheduledFuture);
    }

    /**
     * 获取future
     *
     * @param id 定时任务主键
     * @return ScheduledFuture
     */
    public static ScheduledFuture<?> getScheduledTaskFuture(Long id) {
        return scheduledFutureMap.get(id);
    }

    /**
     * 移除future
     *
     * @param id id
     */
    public static void removeScheduledTaskFuture(Long id) {
        scheduledFutureMap.remove(id);
    }

}
