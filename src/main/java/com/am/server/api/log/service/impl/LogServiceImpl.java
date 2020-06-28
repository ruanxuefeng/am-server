package com.am.server.api.log.service.impl;

import cn.hutool.core.util.IdUtil;
import com.am.server.api.log.dao.nosql.LogDao;
import com.am.server.api.log.pojo.ao.LogListAo;
import com.am.server.api.log.pojo.ao.SaveLogAo;
import com.am.server.api.log.pojo.po.LogDo;
import com.am.server.api.log.pojo.vo.LogListVo;
import com.am.server.api.log.service.LogService;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.util.MongoUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 阮雪峰
 * @date 2018/8/1 14:36
 */
@Service("logService")
public class LogServiceImpl implements LogService {

    private final LogDao logDao;

    private final MongoTemplate mongoTemplate;

    public LogServiceImpl(LogDao logDao, MongoTemplate mongoTemplate) {
        this.logDao = logDao;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(SaveLogAo saveLogAo) {
        logDao.save(
                new LogDo().setId(IdUtil.getSnowflake(1,1).nextId())
                        .setName(saveLogAo.getName())
                        .setOperate(saveLogAo.getOperate())
                        .setMenu(saveLogAo.getMenu())
                        .setIp(saveLogAo.getIp())
                        .setCreateTime(LocalDateTime.now())
        );
    }

    @Override
    public PageVO<LogListVo> list(LogListAo logListAo) {
        PageVO<LogListVo> page = new PageVO<LogListVo>().setPage(logListAo.getPage()).setPageSize(logListAo.getPageSize());

        Query query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "id"));

        Optional<LogListAo> optional = Optional.of(logListAo);

        Optional<LocalDate> startTime = optional.map(LogListAo::getStartDate);
        Optional<LocalDate> endTime = optional.map(LogListAo::getEndDate);

        if (startTime.isPresent() || endTime.isPresent()) {
            Criteria criteria = new Criteria("createTime");

            startTime.ifPresent(time -> criteria.gte(logListAo.getStartDate().atTime(0, 0, 0, 0)));
            endTime.ifPresent(time -> criteria.lte(logListAo.getEndDate().atTime(23, 59, 59, 999)));

            query.addCriteria(criteria);
        }


        optional.map(LogListAo::getName)
                .filter(name -> !name.isEmpty())
                .ifPresent(name -> query.addCriteria(new Criteria("name").regex(MongoUtils.getRegx(logListAo.getName()))));

        optional.map(LogListAo::getMenu)
                .filter(menu -> !menu.isEmpty())
                .ifPresent(menu -> query.addCriteria(new Criteria("menu").regex(MongoUtils.getRegx(logListAo.getMenu()))));

        optional.map(LogListAo::getOperate)
                .filter(operate -> !operate.isEmpty())
                .ifPresent(operate -> query.addCriteria(new Criteria("operate").regex(MongoUtils.getRegx(logListAo.getOperate()))));

        page.setTotal((int) mongoTemplate.count(query, LogDo.class));

        query.skip(page.getCol()).limit(page.getPageSize());
        page.setRows(
                mongoTemplate.find(query, LogDo.class)
                        .stream()
                        .map(
                                log -> new LogListVo()
                                        .setIp(log.getIp())
                                        .setMenu(log.getMenu())
                                        .setOperate(log.getOperate())
                                        .setName(log.getName())
                                        .setCreateTime(log.getCreateTime())
                        ).collect(Collectors.toList())
        );

        page.setTotalPage(Math.toIntExact((page.getTotal() % page.getTotalPage() == 0 ? (page.getTotal() / page.getTotalPage()) : (page.getTotal() / page.getTotalPage() + 1))));
        return page;
    }
}
