package com.am.server.api.upload.dao.rdb.impl;

import com.am.server.api.upload.dao.rdb.SysFileDao;
import com.am.server.api.upload.pojo.po.SysFileDo;
import com.am.server.api.upload.repository.SysFileRepository;
import org.springframework.stereotype.Service;

/**
 * @author 阮雪峰
 * @date 2020年5月17日17:55:49
 */
@Service
public class SysFileDaoImpl implements SysFileDao {
    private final SysFileRepository sysFileRepository;

    public SysFileDaoImpl(SysFileRepository sysFileRepository) {
        this.sysFileRepository = sysFileRepository;
    }

    @Override
    public void save(SysFileDo sysFile) {
        sysFileRepository.save(sysFile);
    }
}
