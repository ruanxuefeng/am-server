package com.am.server.api.admin.log.dao.mongo;

import com.am.server.api.admin.log.entity.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author 阮雪峰
 * @date 2018/8/1 14:37
 */
@Repository("adminLogDao")
public interface LogDao extends MongoRepository<Log, Long> {
}
