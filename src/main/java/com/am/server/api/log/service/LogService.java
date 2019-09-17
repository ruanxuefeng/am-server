package com.am.server.api.log.service;

import com.am.server.api.log.pojo.ao.LogListAO;
import com.am.server.api.log.pojo.ao.SaveLogAO;
import com.am.server.api.log.pojo.vo.LogListVO;
import com.am.server.common.base.pojo.vo.PageVO;

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
    void save(SaveLogAO log);

    /**
     * list
     * @param log log
     * @author 阮雪峰
     * @date 2018/9/17 9:30
     * @return PageVO<LogListVO>
     */
    PageVO<LogListVO> list(LogListAO log);
}
