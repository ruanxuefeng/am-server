package com.am.server.api.task.repository;

import com.am.server.api.task.enumerate.ScheduledTaskStatus;
import com.am.server.api.task.pojo.po.ScheduledTaskDo;
import com.am.server.common.base.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 阮雪峰
 * @date 2020年5月17日17:57:10
 */
@Repository
public interface ScheduledTaskRepository extends BaseRepository<ScheduledTaskDo> {
    /**
     * 通过状态查询所有的定时任务
     * @param status status
     * @return List<ScheduledTaskDo>
     */
    List<ScheduledTaskDo> findAllByStatus(ScheduledTaskStatus status);
}
