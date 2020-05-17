package com.am.server.api.upload.dao.rdb;

import com.am.server.api.upload.pojo.po.SysFileDo;

/**
 * @author 阮雪峰
 * @date 2020年4月25日13:07:44
 */
public interface SysFileDao {
    /**
     * save
     *
     * @param sysFile sysFile
     */
    void save(SysFileDo sysFile);
}
