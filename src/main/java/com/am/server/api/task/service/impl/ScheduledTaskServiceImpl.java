package com.am.server.api.task.service.impl;

import com.am.server.api.task.dao.rdb.ScheduledTaskDao;
import com.am.server.api.task.enumerate.ExecuteStatus;
import com.am.server.api.task.enumerate.ScheduledTaskStatus;
import com.am.server.api.task.execute.ExecuteScheduledTaskService;
import com.am.server.api.task.pojo.ao.SaveScheduledTaskAo;
import com.am.server.api.task.pojo.ao.ScheduledTaskListAo;
import com.am.server.api.task.pojo.ao.UpdateScheduledTaskAo;
import com.am.server.api.task.pojo.po.ScheduledTaskDo;
import com.am.server.api.task.pojo.vo.ScheduledTaskListListVo;
import com.am.server.api.task.service.ScheduledTaskService;
import com.am.server.api.task.thread.ScheduledTaskThread;
import com.am.server.api.task.util.ScheduledTaskUtils;
import com.am.server.api.user.pojo.po.AdminUserDo;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.base.service.CommonService;
import com.am.server.common.util.ThreadUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/**
 * @author 阮雪峰
 * @date 2020年4月26日21:29:18
 */
@Service
public class ScheduledTaskServiceImpl implements ScheduledTaskService {

    private final ScheduledTaskDao scheduledTaskDao;

    private final CommonService commonService;

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private final ApplicationContext applicationContext;

    public ScheduledTaskServiceImpl(ScheduledTaskDao scheduledTaskDao, CommonService commonService, ThreadPoolTaskScheduler threadPoolTaskScheduler, ApplicationContext applicationContext) {
        this.scheduledTaskDao = scheduledTaskDao;
        this.commonService = commonService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.applicationContext = applicationContext;
    }

    @Commit
    @Override
    public void save(SaveScheduledTaskAo saveScheduledTaskAo) {
        ScheduledTaskDo scheduledTask = new ScheduledTaskDo()
                .setCorn(saveScheduledTaskAo.getCorn())
                .setName(saveScheduledTaskAo.getName())
                .setMemo(saveScheduledTaskAo.getMemo())
                .setBean(saveScheduledTaskAo.getBean())
                .setStatus(ScheduledTaskStatus.Disable);

        scheduledTaskDao.save(scheduledTask);
    }

    @ReadOnly
    @Override
    public PageVO<ScheduledTaskListListVo> list(ScheduledTaskListAo scheduledTaskListAo) {
        Page<ScheduledTaskDo> page = scheduledTaskDao.findAll(scheduledTaskListAo);

        return new PageVO<ScheduledTaskListListVo>()
                .setPageSize(scheduledTaskListAo.getPageSize())
                .setPage(scheduledTaskListAo.getPage())
                .setTotal(page.getNumber())
                .setRows(page.getContent().stream()
                        .map(scheduledTask -> new ScheduledTaskListListVo()
                                .setId(scheduledTask.getId())
                                .setName(scheduledTask.getName())
                                .setCorn(scheduledTask.getCorn())
                                .setBean(scheduledTask.getBean())
                                .setMemo(scheduledTask.getMemo())
                                .setStatus(scheduledTask.getStatus())
                                .setTimeConsuming(scheduledTask.getTimeConsuming())
                                .setLatestDate(scheduledTask.getLatestDate())
                                .setCreatorName(Optional.ofNullable(scheduledTask.getCreatedBy()).map(AdminUserDo::getName).orElse(""))
                                .setUpdaterName(Optional.ofNullable(scheduledTask.getUpdatedBy()).map(AdminUserDo::getName).orElse(""))
                                .setCreateTime(scheduledTask.getCreatedTime())
                                .setUpdatedTime(scheduledTask.getUpdatedTime())
                        )
                        .collect(Collectors.toList()));
    }

    @Commit
    @Override
    public void update(UpdateScheduledTaskAo updateScheduledTaskAo) {
        scheduledTaskDao.findById(updateScheduledTaskAo.getId())
                .ifPresent(scheduledTask -> {
                    scheduledTask.setCorn(updateScheduledTaskAo.getCorn())
                            .setName(updateScheduledTaskAo.getName())
                            .setMemo(updateScheduledTaskAo.getMemo())
                            .setBean(updateScheduledTaskAo.getBean());

                    scheduledTaskDao.save(scheduledTask);
                });
    }

    @Commit
    @Override
    public void delete(Long id) {
        scheduledTaskDao.deleteById(id);
    }

    @Commit
    @Override
    public void updateStatus(Long id) {
        scheduledTaskDao.findById(id)
                .ifPresent(scheduledTask -> {
                    ScheduledFuture<?> scheduledFuture = ScheduledTaskUtils.getScheduledTaskFuture(scheduledTask.getId());
                    if (ScheduledTaskStatus.Enable.equals(scheduledTask.getStatus())) {
                        scheduledTask.setStatus(ScheduledTaskStatus.Disable);

                        if (scheduledFuture != null) {
                            scheduledFuture.cancel(true);
                            ScheduledTaskUtils.removeScheduledTaskFuture(scheduledTask.getId());
                        }
                    } else {
                        scheduledTask.setStatus(ScheduledTaskStatus.Enable);
                        startScheduledTask(scheduledTask);
                    }

                    scheduledTaskDao.save(scheduledTask);
                });
    }

    @ReadOnly
    @Override
    public List<ScheduledTaskDo> findAllByStatus(ScheduledTaskStatus status) {
        return scheduledTaskDao.findAllByStatus(status);
    }

    @Commit
    @Override
    public void updateExecutedInfo(Long id, long timeConsuming, LocalDateTime executeTime, ExecuteStatus executeStatus) {
        scheduledTaskDao.findById(id)
                .ifPresent(scheduledTask -> {
                    scheduledTask.setExecuteStatus(executeStatus)
                            .setLatestDate(executeTime)
                            .setTimeConsuming(timeConsuming);
                    scheduledTaskDao.save(scheduledTask);
                });
    }

    @Override
    public void startScheduledTask(ScheduledTaskDo scheduledTask) {
        ExecuteScheduledTaskService executeScheduledTaskService = applicationContext.getBean(scheduledTask.getBean(), ExecuteScheduledTaskService.class);

        Thread thread = new ScheduledTaskThread(scheduledTask, executeScheduledTaskService, this);

        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.schedule(thread, new CronTrigger(scheduledTask.getCorn()));

        ScheduledTaskUtils.addScheduledTaskFuture(scheduledTask.getId(), scheduledFuture);
    }

    @ReadOnly
    @Override
    public void trigger(Long id) {
        scheduledTaskDao.findById(id)
                .ifPresent(scheduledTask -> {
                    ExecuteScheduledTaskService executeScheduledTaskService = applicationContext.getBean(scheduledTask.getBean(), ExecuteScheduledTaskService.class);
                    Thread thread = new ScheduledTaskThread(scheduledTask, executeScheduledTaskService, this);
                    ThreadUtils.execute(thread);
                });
    }
}
