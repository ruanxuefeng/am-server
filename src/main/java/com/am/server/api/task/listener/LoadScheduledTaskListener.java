package com.am.server.api.task.listener;

import com.am.server.api.task.enumerate.ScheduledTaskStatus;
import com.am.server.api.task.pojo.po.ScheduledTaskDo;
import com.am.server.api.task.service.ScheduledTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 阮雪峰
 * @date 2019-10-14 13:22:34
 */
@Slf4j
@Component
public class LoadScheduledTaskListener implements ApplicationListener<ContextRefreshedEvent> {

    private final ScheduledTaskService scheduledTaskService;

    public LoadScheduledTaskListener(ScheduledTaskService scheduledTaskService) {
        this.scheduledTaskService = scheduledTaskService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<ScheduledTaskDo> scheduledTaskList = scheduledTaskService.findAllByStatus(ScheduledTaskStatus.Enable);
        for (ScheduledTaskDo scheduledTask : scheduledTaskList) {
            scheduledTaskService.startScheduledTask(scheduledTask);
        }
    }
}
