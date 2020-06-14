package com.am.server.config.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 *
 * @author 阮雪峰
 * @date 2020年6月8日22:26:00
 */
public class WebSocketAppender extends AppenderBase<ILoggingEvent> {

    public final static String SEND_LOG_FLAG = "SEND_TO";
    public final static String ON = "ON";
    public final static String OFF = "OFF";

    private ApplicationContext applicationContext = null;
    protected Encoder<ILoggingEvent> encoder;

    @Override
    protected void append(ILoggingEvent eventObject) {
        getContext().getProperty(SEND_LOG_FLAG);
        if (ON.equals(getContext().getProperty(SEND_LOG_FLAG)) && applicationContext == null) {
            this.applicationContext = (ApplicationContext) getContext().getObject("SpringApplicationContext");
        }
        Level level = eventObject.getLevel();
        byte[] bytes = this.encoder.encode(eventObject);
        String message = new String(bytes);
        LocalDateTime localDateTime = Instant.ofEpochMilli(eventObject.getTimeStamp()).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();

        SystemLogVo systemLogVo = new SystemLogVo(localDateTime, level.toInt(), level.levelStr, message);

        SimpMessagingTemplate simpMessagingTemplate = applicationContext.getBean(SimpMessagingTemplate.class);
        simpMessagingTemplate.convertAndSend("/topic/log", systemLogVo);
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        LayoutWrappingEncoder<ILoggingEvent> lwe = new LayoutWrappingEncoder<>();
        lwe.setLayout(layout);
        lwe.setContext(context);
        this.encoder = lwe;
    }
}
