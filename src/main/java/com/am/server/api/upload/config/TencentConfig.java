package com.am.server.api.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *  腾讯cos配置
 * @author 阮雪峰
 * @date 2018/8/24 16:04
 */
@Data
@Component
@ConfigurationProperties(prefix = "tencent.cos")
public class TencentConfig {
    private String secretId;

    private String secretKey;

    private String regionName;

    private String bucketName;

    private Long appId;
}
