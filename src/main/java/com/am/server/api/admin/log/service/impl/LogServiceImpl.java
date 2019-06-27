package com.am.server.api.admin.log.service.impl;

import com.am.server.api.admin.log.dao.mongo.LogDao;
import com.am.server.api.admin.log.pojo.Log;
import com.am.server.api.admin.log.pojo.ao.LogListAO;
import com.am.server.api.admin.log.pojo.ao.SaveLogAO;
import com.am.server.api.admin.log.pojo.po.LogPO;
import com.am.server.api.admin.log.pojo.vo.LogListVO;
import com.am.server.api.admin.log.service.LogService;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.util.IdUtils;
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
    public void save(SaveLogAO saveLogAo) {
        logDao.save(
                new LogPO().setId(IdUtils.getId())
                        .setName(saveLogAo.getName())
                        .setOperate(saveLogAo.getOperate())
                        .setMenu(saveLogAo.getMenu())
                        .setIp(saveLogAo.getIp())
                        .setCreateTime(LocalDateTime.now())
        );
    }

    @Override
    public PageVO<LogListVO> list(LogListAO logListAo) {
        PageVO<LogListVO> page = new PageVO<LogListVO>().setPage(logListAo.getPage()).setPageSize(logListAo.getPageSize());

        Query query = new Query()
                .with(new Sort(Sort.Direction.DESC, "id"))
                .skip(page.getCol())
                .limit(page.getPageSize());

        Optional<LogListAO> optional = Optional.of(logListAo);

        Optional<LocalDate> startTime = optional.map(LogListAO::getStartDate);
        Optional<LocalDate> endTime = optional.map(LogListAO::getEndDate);

        if (startTime.isPresent() || endTime.isPresent()) {
            Criteria criteria = new Criteria("createTime");

            startTime.ifPresent(time -> criteria.gte(logListAo.getStartDate().atTime(0, 0, 0, 0)));
            endTime.ifPresent(time -> criteria.lte(logListAo.getEndDate().atTime(23, 59, 59, 999)));

            query.addCriteria(criteria);
        }


        optional.map(LogListAO::getName)
                .filter(name -> !name.isEmpty())
                .ifPresent(name -> query.addCriteria(new Criteria("name").regex(MongoUtils.getRegx(logListAo.getName()))));

        optional.map(LogListAO::getMenu)
                .filter(menu -> !menu.isEmpty())
                .ifPresent(menu -> query.addCriteria(new Criteria("menu").regex(MongoUtils.getRegx(logListAo.getMenu()))));

        optional.map(LogListAO::getOperate)
                .filter(operate -> !operate.isEmpty())
                .ifPresent(operate -> query.addCriteria(new Criteria("operate").regex(MongoUtils.getRegx(logListAo.getOperate()))));

        page.setRows(
                mongoTemplate.find(query, LogPO.class)
                        .stream()
                        .map(
                                log -> new LogListVO()
                                        .setIp(log.getIp())
                                        .setMenu(log.getMenu())
                                        .setOperate(log.getOperate())
                                        .setName(log.getName())
                                        .setCreateTime(log.getCreateTime())
                        ).collect(Collectors.toList())
        );
        page.setTotal((int) mongoTemplate.count(query, Log.class));

        return page;
    }
}
