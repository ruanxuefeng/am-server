package com.am.server.api.task.thread;

import com.am.server.api.task.enumerate.ExecuteStatus;
import com.am.server.api.task.execute.ExecuteScheduledTaskService;
import com.am.server.api.task.pojo.po.ScheduledTaskDo;
import com.am.server.api.task.service.ScheduledTaskService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 定时任务线程类
 *
 * @author 阮雪峰
 * @date 2020年4月29日20:13:36
 */
@Slf4j
public class ScheduledTaskThread extends Thread {
    private final ExecuteScheduledTaskService executeScheduledTaskService;

    private final ScheduledTaskService scheduledTaskService;

    /**
     * 任务主键
     */
    private final Long key;

    public ScheduledTaskThread(ScheduledTaskDo scheduledTask, ExecuteScheduledTaskService executeScheduledTaskService, ScheduledTaskService scheduledTaskService) {
        this.executeScheduledTaskService = executeScheduledTaskService;
        this.scheduledTaskService = scheduledTaskService;
        this.key = scheduledTask.getId();
    }

    @Override
    public void run() {
        ExecuteStatus executeStatus = ExecuteStatus.FAILURE;
        long startTime = System.currentTimeMillis();
        LocalDateTime dateTime = LocalDateTime.now();
        try {
            executeScheduledTaskService.execute();
            executeStatus = ExecuteStatus.SUCCESS;
        } catch (Exception e) {
            log.error("执行定时任务[{}({})]发生异常", getName(), key);
            log.error("Execute Scheduled Exception", e);
        } finally {
            long endTime = System.currentTimeMillis();
            scheduledTaskService.updateExecutedInfo(key, (endTime - startTime) / 1000, dateTime, executeStatus);
        }
    }
}
