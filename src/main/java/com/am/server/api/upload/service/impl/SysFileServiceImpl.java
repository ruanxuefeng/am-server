package com.am.server.api.upload.service.impl;

import com.am.server.api.upload.dao.rdb.SysFileDao;
import com.am.server.api.upload.enumerate.FileType;
import com.am.server.api.upload.pojo.po.SysFileDo;
import com.am.server.api.upload.service.FileUploadService;
import com.am.server.api.upload.service.SysFileService;
import com.am.server.common.base.service.CommonService;
import com.am.server.common.util.FileUtils;
import com.am.server.common.util.IdUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
    private static final String DEFAULT_FILE_SUFFIX = ".jpg";

    private final FileUploadService fileUploadService;

    private final CommonService commonService;

    private final SysFileDao sysFileDao;

    public SysFileServiceImpl(FileUploadService fileUploadService, CommonService commonService, SysFileDao sysFileDao) {
        this.fileUploadService = fileUploadService;
        this.commonService = commonService;
        this.sysFileDao = sysFileDao;
    }

    @Override
    public SysFileDo save(MultipartFile file, FileType type) {
        String key = type.getFolder() + "/" + IdUtils.getId() + FileUtils.getFileSuffix(StringUtils.isEmpty(file.getOriginalFilename()) ? file.getOriginalFilename() : DEFAULT_FILE_SUFFIX);
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
