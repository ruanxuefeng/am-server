package com.am.server.api.admin.user.config;

import com.am.server.api.admin.user.service.UserPermissionCacheService;
import com.am.server.api.admin.user.service.impl.RedisUserPermissionCacheServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用户相关配置
 *
 * @author 阮雪峰
 * @date 2019/3/27 13:44
 */
@Configuration
public class UserConfig {
    @Bean
    public UserPermissionCacheService userPermissionCacheService() {
        return new RedisUserPermissionCacheServiceImpl();
    }
}
