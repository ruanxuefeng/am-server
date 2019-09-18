package com.am.server.api.permission.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 阮雪峰
 * @date 2019年9月18日17:06:55
 */
@Configuration
@ConfigurationProperties(prefix = "permission")
@Data
public class PermissionConfig {
    /**
     * 需要扫描权限的包名
     * 不支持表达式匹配
     */
    private String basePackage;
}
