package com.am.server.api.task.service;

import com.am.server.api.task.enumerate.ExecuteStatus;
import com.am.server.api.task.enumerate.ScheduledTaskStatus;
import com.am.server.api.task.pojo.ao.SaveScheduledTaskAo;
import com.am.server.api.task.pojo.ao.ScheduledTaskListAo;
import com.am.server.api.task.pojo.ao.UpdateScheduledTaskAo;
import com.am.server.api.task.pojo.po.ScheduledTaskDo;
import com.am.server.api.task.pojo.vo.ScheduledTaskListListVo;
import com.am.server.common.base.pojo.vo.PageVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 阮雪峰
 * @date 2020年4月26日21:04:30
 */
public interface ScheduledTaskService {
    /**
     * 新增
     * @param saveScheduledTaskAo saveScheduledTaskAo
     */
    void save(SaveScheduledTaskAo saveScheduledTaskAo);

    /**
     * 分页查询
     * @param scheduledTaskListAo  scheduledTaskListAo
     * @return PageVO<ScheduledTaskListListVo>
     */
    PageVO<ScheduledTaskListListVo> list(ScheduledTaskListAo scheduledTaskListAo);

    /**
     * 更新定时任务
     * @param updateScheduledTaskAo updateScheduledTaskAo
     */
    void update(UpdateScheduledTaskAo updateScheduledTaskAo);

    /**
     * 删除任务，不是物理删除，只是将状态该为删除
     * @param id 主键
     */
    void delete(Long id);

    /**
     * 修改状态
     * 如果现在是启用状态则改为禁用状态，如果是禁用状态则改为启用状态
     * @param id 主键
     */
    void updateStatus(Long id);

    /**
     * 查询当前所有可用的定时任务
     * @param status status
     * @return List<ScheduledTaskDo>
     */
    List<ScheduledTaskDo> findAllByStatus(ScheduledTaskStatus status);

    /**
     * 更新定时任务执行信息
     *
     * @param id            主键
     * @param executeTime   执行耗时
     * @param timeConsuming 执行时间
     * @param executeStatus 执行状态
     */
    void updateExecutedInfo(Long id, long timeConsuming, LocalDateTime executeTime, ExecuteStatus executeStatus);

    /**
     * 启动定时任务
     * @param scheduledTask scheduledTask
     */
    void startScheduledTask(ScheduledTaskDo scheduledTask);

    /**
     * 直接触发定时任务
     * @param id 主键
     */
    void trigger(Long id);
}
