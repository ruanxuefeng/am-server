package com.am.server.api.task.execute;

/**
 * 所有定时任务的接口
 *
 * @author 阮雪峰
 * @date 2020年4月29日19:51:37
 */
public interface ExecuteScheduledTaskService {

    /**
     * 执行任务
     */
    void execute();
}
