package com.am.server.api.upload.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.am.server.api.upload.enumerate.FileType;
import com.am.server.api.upload.pojo.po.SysFileDo;
import com.am.server.api.upload.service.FileUploadService;
import com.am.server.api.upload.service.SysFileService;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.base.service.CommonService;
import com.am.server.config.sys.IdConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 阮雪峰
 * @date 2020年4月25日13:05:04
 */
@Service
public class SysFileServiceImpl implements SysFileService {
    /**
     * 默认文件名后缀
     */
    private static final String DEFAULT_FILE_SUFFIX = "jpg";

    private final FileUploadService fileUploadService;

    private final IdConfig idConfig;

    private final CommonService commonService;

    public SysFileServiceImpl(FileUploadService fileUploadService, IdConfig idConfig, CommonService commonService) {
        this.fileUploadService = fileUploadService;
        this.idConfig = idConfig;
        this.commonService = commonService;
    }

    @Commit
    @Override
    public SysFileDo save(MultipartFile file, FileType type) {
        SysFileDo sysFileDo = new SysFileDo();
        commonService.beforeSave(sysFileDo);
        //获取文件后缀名
        String suffix = StrUtil.isEmpty(file.getOriginalFilename()) ? StrUtil.subAfter(file.getOriginalFilename(), ".", true) : DEFAULT_FILE_SUFFIX;
        String key = type.getFolder() + "/" + sysFileDo.getId() + "." + suffix;
        String url = fileUploadService.upload(file, key);

        return sysFileDo.setType(type)
                .setDir(key)
                .setUrl(url);
    }

    @Commit
    @Override
    public SysFileDo updateFileContent(MultipartFile file, SysFileDo sysFile, FileType avatar) {
        if (sysFile == null) {
            return save(file, avatar);
        }
        String url = fileUploadService.upload(file, sysFile.getDir());
        sysFile.setUrl(url);

        return sysFile;
    }
}
