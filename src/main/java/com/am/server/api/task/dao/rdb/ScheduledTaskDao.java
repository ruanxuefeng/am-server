package com.am.server.api.task.dao.rdb;

import com.am.server.api.task.enumerate.ScheduledTaskStatus;
import com.am.server.api.task.pojo.ao.ScheduledTaskListAo;
import com.am.server.api.task.pojo.po.ScheduledTaskDo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author 阮雪峰
 * @date 2020年4月26日21:30:03
 */
public interface ScheduledTaskDao {
    /**
     * 通过状态查询所有的定时任务
     *
     * @param status status
     * @return List<ScheduledTaskDo>
     */
    List<ScheduledTaskDo> findAllByStatus(ScheduledTaskStatus status);

    /**
     * save
     *
     * @param scheduledTask scheduledTask
     */
    void save(ScheduledTaskDo scheduledTask);

    /**
     * 分页
     *
     * @param scheduledTaskListAo scheduledTaskListAo
     * @return Page<ScheduledTaskDo>
     */
    Page<ScheduledTaskDo> findAll(ScheduledTaskListAo scheduledTaskListAo);

    /**
     * find by id
     *
     * @param id id
     * @return Optional<ScheduledTaskDo>
     */
    Optional<ScheduledTaskDo> findById(Long id);

    /**
     * delete by id
     *
     * @param id id
     */
    void deleteById(Long id);
}
