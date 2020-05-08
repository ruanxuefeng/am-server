package com.am.server.api.upload.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.am.server.api.upload.dao.rdb.SysFileDao;
import com.am.server.api.upload.enumerate.FileType;
import com.am.server.api.upload.pojo.po.SysFileDo;
import com.am.server.api.upload.service.FileUploadService;
import com.am.server.api.upload.service.SysFileService;
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

    private final CommonService commonService;

    private final SysFileDao sysFileDao;

    private final IdConfig idConfig;

    public SysFileServiceImpl(FileUploadService fileUploadService, CommonService commonService, SysFileDao sysFileDao, IdConfig idConfig) {
        this.fileUploadService = fileUploadService;
        this.commonService = commonService;
        this.sysFileDao = sysFileDao;
        this.idConfig = idConfig;
    }

    @Override
    public SysFileDo save(MultipartFile file, FileType type) {
        //获取文件后缀名
        String suffix = StrUtil.isEmpty(file.getOriginalFilename()) ? StrUtil.subAfter(file.getOriginalFilename(), ".", true) : DEFAULT_FILE_SUFFIX;
        String key = type.getFolder() + "/" + IdUtil.getSnowflake(idConfig.getWorkerId(), idConfig.getDataCenterId()).nextId() + "." + suffix;
        String url = fileUploadService.upload(file, key);
        SysFileDo sysFile = new SysFileDo().setType(type)
                .setDir(key)
                .setUrl(url);

        commonService.beforeSave(sysFile);
        sysFileDao.save(sysFile);
        return sysFile;
    }

    @Override
    public void updateFileContent(MultipartFile file, SysFileDo sysFile) {
        fileUploadService.upload(file, sysFile.getDir());
    }
}
