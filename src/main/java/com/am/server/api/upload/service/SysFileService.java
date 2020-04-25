package com.am.server.api.upload.service;

import com.am.server.api.upload.enumerate.FileType;
import com.am.server.api.upload.pojo.po.SysFileDo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 系统文件
 *
 * @author 阮雪峰
 * @date 2020年4月25日13:02:23
 */
@Service
public interface SysFileService {
    /**
     * 保存系统文件
     *
     * @param file 上传的文件
     * @param type 文件类型
     * @return SysFile
     */
    SysFileDo save(MultipartFile file, FileType type);

    /**
     * 修改文件内容
     *
     * @param file    用户上传的新闻文件
     * @param sysFile 本地原来的文件
     */
    void updateFileContent(MultipartFile file, SysFileDo sysFile);
}
