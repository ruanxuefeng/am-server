package com.am.server.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 *
 * @author 阮雪峰
 * @date 2018/11/15 9:45
 */
@Slf4j
@Component
public class WebSocketEventListener {
    @EventListener
    public void handleConnectListener(SessionConnectedEvent event) {
        log.info("[ws-connected] socket connect: {}", event.getMessage());
    }

    @EventListener
    public void handleDisconnectListener(SessionDisconnectEvent event) {
        log.info("[ws-disconnect] socket disconnect: {}", event.getMessage());
    }
}
