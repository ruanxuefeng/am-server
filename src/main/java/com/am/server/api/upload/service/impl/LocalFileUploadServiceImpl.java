package com.am.server.api.upload.service.impl;

import com.am.server.api.upload.config.LocalFileUploadConfig;
import com.am.server.api.upload.exception.UploadFileException;
import com.am.server.api.upload.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * 本地文件上传
 *
 * @author 阮雪峰
 * @date 2019/4/11 10:16
 */
@Slf4j
public class LocalFileUploadServiceImpl implements FileUploadService {

    @Resource
    private LocalFileUploadConfig config;

    @Override
    public String upload(MultipartFile file, String key) {

        try {
            File file1 = new File(config.getFilePath() + key);
            if (!file1.getParentFile().exists() && file1.getParentFile().mkdirs()) {
                log.info("创建文件夹成功（{}）", config.getFilePath() + key);
            }
            file.transferTo(file1);
            return config.getRequestUrl() + config.getUri() + key + "?" + System.currentTimeMillis();
        } catch (IOException e) {
            throw new UploadFileException(e);
        }
    }

    @Override
    public void remove(String key) {
        File f = new File(key);
        if (f.exists() && f.delete()) {
            log.info("文件删除成功（{}）", key);
        }
    }
}
