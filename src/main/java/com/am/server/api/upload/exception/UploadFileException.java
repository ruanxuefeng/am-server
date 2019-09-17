package com.am.server.api.upload.exception;

import java.io.IOException;

/**
 *  文件上传失败
 * @author 阮雪峰
 * @date 2018/8/24 16:42
 */
public class UploadFileException extends RuntimeException {
    public UploadFileException(IOException e) {
        super(e);
    }
}
