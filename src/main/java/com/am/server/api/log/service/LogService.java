package com.am.server.api.log.service;

import com.am.server.api.log.pojo.ao.LogListAo;
import com.am.server.api.log.pojo.ao.SaveLogAo;
import com.am.server.api.log.pojo.vo.LogListVo;
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
    void save(SaveLogAo log);

    /**
     * list
     * @param log log
     * @author 阮雪峰
     * @date 2018/9/17 9:30
     * @return PageVO<LogListVO>
     */
    PageVO<LogListVo> list(LogListAo log);
}
