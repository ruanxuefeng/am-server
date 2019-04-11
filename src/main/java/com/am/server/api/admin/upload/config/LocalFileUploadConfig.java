package com.am.server.api.admin.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 本地文件上传配置
 * @date 2019/4/11 10:04
 * @author 阮雪峰
 */
@ConfigurationProperties(prefix = "local.file")
@Configuration
@Data
public class LocalFileUploadConfig implements WebMvcConfigurer {
    /**
     * 存储文件的路径
     */
    private String filePath;
    /**
     * 拦截请求文件的路径
     */
    private String uri;
    /**
     * 上传文件后的请求路径
     */
    private String requestUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * @Description: 对文件的路径进行配置, 创建一个虚拟路径/file/** ，即只要在<img src="/file/images/20180522/9aa64b2b-a558-421e-929c-537ff0aecdba.jpg" />便可以直接引用图片
         *这是图片的物理路径 "file:/+本地图片的地址"
         * @Date： Create in 14:08 2017/12/20
         *
         */
        //读取配置文件中的上传路径
        registry.addResourceHandler(uri + "/**").addResourceLocations("file:" + filePath);
    }
}
