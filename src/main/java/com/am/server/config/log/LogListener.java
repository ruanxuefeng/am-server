package com.am.server.config.log;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author 阮雪峰
 * @date 2019-10-14 13:22:34
 */
@Component
public class LogListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.putProperty(WebSocketAppender.SEND_LOG_FLAG, WebSocketAppender.ON);
        lc.putObject("SpringApplicationContext", event.getApplicationContext());
    }
}
