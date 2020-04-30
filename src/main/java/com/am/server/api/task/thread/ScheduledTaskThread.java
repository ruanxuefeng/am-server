package com.am.server.api.task.thread;

import com.am.server.api.task.dao.rdb.ScheduledTaskDao;
import com.am.server.api.task.enumerate.ExecuteStatus;
import com.am.server.api.task.execute.ExecuteScheduledTaskService;
import com.am.server.api.task.pojo.po.ScheduledTaskDo;
import com.am.server.api.task.service.ScheduledTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;

/**
 * 定时任务线程类
 *
 * @author 阮雪峰
 * @date 2020年4月29日20:13:36
 */
@Slf4j
public class ScheduledTaskThread extends Thread {
    private final ApplicationContext applicationContext;

    private final ExecuteScheduledTaskService executeScheduledTaskService;

    private final ScheduledTaskService scheduledTaskService;

    /**
     * 任务主键
     */
    private final Long key;

    public ScheduledTaskThread(ApplicationContext applicationContext, ScheduledTaskDo scheduledTask, ExecuteScheduledTaskService executeScheduledTaskService, ScheduledTaskService scheduledTaskService) {
        this.applicationContext = applicationContext;
        this.executeScheduledTaskService = executeScheduledTaskService;
        this.scheduledTaskService = scheduledTaskService;
        this.key = scheduledTask.getId();
//        this.setName(scheduledTask.getName());
    }

    @Override
    public void run() {
        ExecuteStatus executeStatus = ExecuteStatus.Failure;
        long startTime = System.currentTimeMillis();
        LocalDateTime dateTime = LocalDateTime.now();
        try {
            executeScheduledTaskService.execute();
            executeStatus = ExecuteStatus.Success;
        } catch (Exception e) {
            log.error("执行定时任务[{}({})]发生异常", getName(), key);
            log.error("Execute Scheduled Exception", e);
        } finally {
            long endTime = System.currentTimeMillis();
            scheduledTaskService.updateExecutedInfo(key, (endTime - startTime) / 1000, dateTime, executeStatus);
        }
    }
}
