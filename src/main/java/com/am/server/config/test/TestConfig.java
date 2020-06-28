package com.am.server.config.test;

import com.am.server.common.util.JwtUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 阮雪峰
 * @date 2020年6月28日13:27:55
 */
@ConfigurationProperties(prefix = "test")
@Configuration
public class TestConfig {
    @Getter
    @Setter
    private String uid;
    @Getter
    @Setter
    private String username;
    /**
     * 为了简单，使用base64加密后的字符串
     */
    @Getter
    @Setter
    private String password;

    @Bean
    public TestConfig testEntity() {
        return new TestConfig();
    }
}
