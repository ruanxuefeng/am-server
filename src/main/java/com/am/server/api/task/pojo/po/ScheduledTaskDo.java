package com.am.server.api.task.pojo.po;

import com.am.server.api.task.enumerate.ExecuteStatus;
import com.am.server.api.task.enumerate.ScheduledTaskStatus;
import com.am.server.common.base.pojo.po.BaseDo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 定时任务
 *
 * @author 阮雪峰
 * @date 2020年4月26日20:46:59
 */
@Accessors(chain = true)
@Setter
@Getter
@Entity
@Table(name = "scheduled_task")
public class ScheduledTaskDo extends BaseDo {
    /**
     * 名称
     */
    @Column
    private String name;

    @Column
    private String corn;

    /**
     * 状态
     */
    @Column
    private ScheduledTaskStatus status;

    /**
     * Spring的bean名称
     */
    @Column
    private String bean;

    /**
     * 备注
     */
    @Column
    private String memo;

    /**
     * 上次执行日期
     */
    @Column
    private LocalDateTime latestDate;

    /**
     * 执行耗时(秒)
     */
    @Column
    private Long timeConsuming;

    @Column
    private ExecuteStatus executeStatus;
}
