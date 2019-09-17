package com.am.server.api.bulletin.interceptor;


import com.am.server.common.constant.Constant;
import com.am.server.common.util.JwtUtils;
import com.sun.security.auth.UserPrincipal;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.StringUtils;

import java.security.Principal;

/**
 *
 * @author 阮雪峰
 * @date 2018/11/15 9:58
 */
public class WebSocketHandleInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader(Constant.TOKEN);
            if (StringUtils.isEmpty(token)) {
                return null;
            }
            // 绑定user
            Principal principal = new UserPrincipal(JwtUtils.getSubject(token));
            accessor.setUser(principal);
        }
        return message;
    }
}
