package com.am.server.api.task.dao.rdb;

import com.am.server.api.task.enumerate.ScheduledTaskStatus;
import com.am.server.api.task.pojo.po.ScheduledTaskDo;
import com.am.server.common.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 阮雪峰
 * @date 2020年4月26日21:30:03
 */
@Repository
public interface ScheduledTaskDao extends BaseDao<ScheduledTaskDo> {
    /**
     * 通过状态查询所有的定时任务
     * @param status status
     * @return List<ScheduledTaskDo>
     */
    List<ScheduledTaskDo> findAllByStatus(ScheduledTaskStatus status);
}
