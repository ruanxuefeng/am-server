package com.am.server.api.admin.log.service.impl;

import com.am.server.api.admin.log.dao.mongo.LogDao;
import com.am.server.api.admin.log.entity.Log;
import com.am.server.api.admin.log.service.LogService;
import com.am.server.advice.update.annotation.Save;
import com.am.server.common.base.entity.PageVO;
import com.am.server.common.util.MongoUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

/**
 *
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

    @Save
    @Override
    public void save(Log log) {
        logDao.save(log);
    }

    @Override
    public void list(PageVO<Log> page, Log log) {

        Query query = new Query()
                .with(new Sort(Sort.Direction.DESC, "id"))
                .skip(page.getCol())
                .limit(page.getPageSize());

        Optional<Log> optional = Optional.ofNullable(log);
        Optional<LocalDate> startTime = optional.map(Log::getStartDate);


        Optional<LocalDate> endTime = optional.map(Log::getEndDate);

        if (startTime.isPresent() || endTime.isPresent()) {
            Criteria criteria = new Criteria("createTime");

            startTime.ifPresent(time -> criteria.gte(log.getStartDate().atTime(0,0,0,0)));
            endTime.ifPresent(time -> criteria.lte(log.getEndDate().atTime(23,59,59,999)));

            query.addCriteria(criteria);
        }


        optional.map(Log::getName)
                .filter(name -> !name.isEmpty())
                .ifPresent(name -> query.addCriteria(new Criteria("name").regex(MongoUtils.getRegx(log.getName()))));

        optional.map(Log::getMenu)
                .filter(menu -> !menu.isEmpty())
                .ifPresent(menu -> query.addCriteria(new Criteria("menu").regex(MongoUtils.getRegx(log.getMenu()))));

        optional.map(Log::getOperate)
                .filter(operate -> !operate.isEmpty())
                .ifPresent(operate -> query.addCriteria(new Criteria("operate").regex(MongoUtils.getRegx(log.getOperate()))));

        page.setRows(mongoTemplate.find(query, Log.class));
        page.setTotal((int) mongoTemplate.count(query, Log.class));
    }
}
