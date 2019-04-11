package com.am.server.api.admin.log.service;

import com.am.server.api.admin.log.entity.Log;
import com.am.server.common.base.page.Page;

/**
 *
 * @author 阮雪峰
 * @date 2018/8/1 14:35
 */
public interface LogService {
    /**
     * 保存日志
     * @param log log
     * @author 阮雪峰
     * @date 2018/8/1 14:36
     */
    void save(Log log);

    /**
     * list
     * @param page page
     * @param log log
     * @author 阮雪峰
     * @date 2018/9/17 9:30
     */
    void list(Page<Log> page, Log log);
}
