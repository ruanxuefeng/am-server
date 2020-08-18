package com.am.server.api.log.dao.rbd.impl;

import cn.hutool.core.util.StrUtil;
import com.am.server.api.log.dao.rbd.LogDao;
import com.am.server.api.log.pojo.ao.LogListAo;
import com.am.server.api.log.pojo.po.LogDo;
import com.am.server.api.log.repository.LogRepository;
import com.am.server.common.constant.QueryConstant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

/**
 * @author 阮雪峰
 * @date 2020年8月18日20:04:55
 */
@Service
public class LogDaoImpl implements LogDao {

    private final LogRepository logRepository;

    public LogDaoImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public void save(LogDo log) {
        logRepository.save(log);
    }

    @Override
    public Page<LogDo> findAll(LogListAo logListAo) {
        return logRepository.findAll(
                (root, query, cb) -> {
                    Predicate predicate = cb.conjunction();
                    Optional.ofNullable(logListAo.getName())
                            .filter(StrUtil::isNotBlank)
                            .ifPresent(name -> predicate.getExpressions().add(cb.like(root.get("name"), String.format(QueryConstant.FULL_LIKE_QUERY_FORMAT, logListAo.getName()))));

                    Optional.ofNullable(logListAo.getOperate())
                            .filter(StrUtil::isNotBlank)
                            .ifPresent(operate -> predicate.getExpressions().add(cb.like(root.get("operate"), String.format(QueryConstant.FULL_LIKE_QUERY_FORMAT, logListAo.getOperate()))));

                    Optional.ofNullable(logListAo.getMenu())
                            .filter(StrUtil::isNotBlank)
                            .ifPresent(menu -> predicate.getExpressions().add(cb.like(root.get("menu"), String.format(QueryConstant.FULL_LIKE_QUERY_FORMAT, logListAo.getMenu()))));

                    if (logListAo.getStartDate() != null && logListAo.getEndDate() != null) {
                        LocalDateTime startDateTime = LocalDateTime.of(logListAo.getStartDate(), LocalTime.MIN);
                        LocalDateTime endDateTime = LocalDateTime.of(logListAo.getStartDate(), LocalTime.MAX);
                        predicate.getExpressions().add(cb.between(root.get("createdTime"), startDateTime, endDateTime));
                    }
                    return predicate;
                },
                PageRequest.of(logListAo.getPage() - 1, logListAo.getPageSize()));
    }
}
