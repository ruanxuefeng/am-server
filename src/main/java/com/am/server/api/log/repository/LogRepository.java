package com.am.server.api.log.repository;

import com.am.server.api.log.pojo.po.LogDo;
import com.am.server.common.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 阮雪峰
 * @date 2020年8月18日19:47:14
 */
@Repository
public interface LogRepository extends BaseRepository<LogDo> {
}
