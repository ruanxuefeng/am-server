package com.am.server.api.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 任务管理相关配置
 * @author 阮雪峰
 * @date 2020年4月29日19:41:04
 */
@Configuration
public class TaskConfig {
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        // 线程池数量
        executor.setPoolSize(Runtime.getRuntime().availableProcessors());
        //设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix("ScheduledTask-");
        //用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //该方法用来设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        executor.setAwaitTerminationSeconds(300);
        return executor;
    }
}
