package com.am.server.config.sys;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 系统相关配置
 *
 * @author 阮雪峰
 * @date 2020年5月8日21:33:27
 */
@ConfigurationProperties(prefix = "sys.id")
@Configuration
@Getter
@Setter
public class IdConfig {
    /**
     * 终端ID
     */
    private Long workerId;
    /**
     * 数据中心ID
     */
    private Long dataCenterId;
}
