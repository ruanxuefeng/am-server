package com.am.server.api.log.service.impl;

import com.am.server.api.log.dao.rbd.LogDao;
import com.am.server.api.log.pojo.ao.LogListAo;
import com.am.server.api.log.pojo.ao.SaveLogAo;
import com.am.server.api.log.pojo.po.LogDo;
import com.am.server.api.log.pojo.vo.LogListVo;
import com.am.server.api.log.service.LogService;
import com.am.server.common.base.pojo.vo.PageVO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * @author 阮雪峰
 * @date 2018/8/1 14:36
 */
@Service("logService")
public class LogServiceImpl implements LogService {

    private final LogDao logDao;

    public LogServiceImpl(LogDao logDao) {
        this.logDao = logDao;
    }

    @Override
    public void save(SaveLogAo saveLogAo) {
        LogDo log = new LogDo()
                .setName(saveLogAo.getName())
                .setOperate(saveLogAo.getOperate())
                .setMenu(saveLogAo.getMenu())
                .setIp(saveLogAo.getIp());
        logDao.save(log);
    }

    @Override
    public PageVO<LogListVo> list(LogListAo logListAo) {
        Page<LogDo> page = logDao.findAll(logListAo);
        return new PageVO<LogListVo>()
                .setPageSize(logListAo.getPageSize())
                .setPage(logListAo.getPage())
                .setTotal((int) page.getTotalElements())
                .setTotalPage(page.getTotalPages())
                .setRows(page.getContent().stream()
                        .map(log -> new LogListVo()
                                .setCreateTime(log.getCreatedTime())
                                .setIp(log.getIp())
                                .setMenu(log.getMenu())
                                .setName(log.getName())
                                .setOperate(log.getOperate())
                        )
                        .collect(Collectors.toList()));
    }
}
