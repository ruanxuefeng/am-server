package com.am.server.api.upload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 *  文件上传
 * @author 阮雪峰
 * @date 2018/8/24 15:46
 */
public interface FileUploadService {

    /**
     * 文件上传
     * @param file 文件
     * @param key 上传路径+文件名
     * @return java.lang.String
     * @author 阮雪峰
     * @date 2018/8/24 17:26
     */
    String upload(MultipartFile file, String key);

    /**
     * 移除文件
     * @param key 路径
     * @author 阮雪峰
     * @date 2019年6月26日14:33:10
     */
    void remove(String key);
}
