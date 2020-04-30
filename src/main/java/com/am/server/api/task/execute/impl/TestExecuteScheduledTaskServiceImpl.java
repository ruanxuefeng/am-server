package com.am.server.api.task.execute.impl;

import com.am.server.api.task.execute.ExecuteScheduledTaskService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 测试定时任务
 *
 * @author 阮雪峰
 * @date 2020年4月29日19:53:43
 */
@Setter
@Getter
@Slf4j
@Service("TestScheduledTask")
public class TestExecuteScheduledTaskServiceImpl implements ExecuteScheduledTaskService {

    @Override
    public void execute() {
        log.info("Just a Test unit!");
    }
}
