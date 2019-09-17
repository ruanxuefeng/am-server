package com.am.server.api.upload.config;

import com.am.server.api.upload.service.FileUploadService;
import com.am.server.api.upload.service.impl.LocalFileUploadServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件上传服务配置
 * @date 2019/4/11 10:22
 * @author 阮雪峰
 */
@Configuration
public class FileUploadConfig {
    @Bean
    public FileUploadService fileUploadService() {
        return new LocalFileUploadServiceImpl();
    }
}
