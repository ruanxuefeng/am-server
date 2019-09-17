package com.am.server.api.log.dao.nosql;

import com.am.server.api.log.pojo.po.LogPO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author 阮雪峰
 * @date 2018/8/1 14:37
 */
@Repository("adminLogDao")
public interface LogDao extends MongoRepository<LogPO, Long> {
}
