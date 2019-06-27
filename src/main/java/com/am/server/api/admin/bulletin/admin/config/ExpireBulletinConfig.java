package com.am.server.api.admin.bulletin.admin.config;

import com.am.server.api.admin.bulletin.admin.pojo.BulletinExpiredDelayedImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.DelayQueue;

/**
 * @author 阮雪峰
 * @date 2018/11/15 16:25
 */
@Configuration
public class ExpireBulletinConfig {
    @Bean
    public DelayQueue<BulletinExpiredDelayedImpl> delayQueue() {
        return new DelayQueue<>();
    }
}
