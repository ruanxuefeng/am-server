package com.am.server.api.bulletin.config;

import com.am.server.api.bulletin.interceptor.WebSocketHandleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 *
 * @author 阮雪峰
 * @date 2018/11/14 16:52
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册STOMP端点，即WebSocket客户端需要连接到WebSocket握手端点
        //这是一个端点，客户端在订阅或发布消息到目的地路径前，要连接该端点
        registry.addEndpoint("/point")
                //跨域设置
                .setAllowedOrigins("*")
                //启用SockJS功能
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //设置消息代理，所有目的地前缀为"/topic","/queue"的消息都会发送到STOMP代理中
        registry.enableSimpleBroker("/topic");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new WebSocketHandleInterceptor());
    }
}
