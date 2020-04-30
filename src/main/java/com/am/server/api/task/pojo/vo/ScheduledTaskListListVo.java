package com.am.server.api.task.pojo.vo;

import com.am.server.api.task.enumerate.ScheduledTaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 列表展示实体
 *
 * @author 阮雪峰
 * @date 2020年4月26日21:18:29
 */
@ApiModel
@Accessors(chain = true)
@Setter
@Getter
public class ScheduledTaskListListVo {
    @ApiModelProperty(value = "主键", example = "1234567890")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "corn表达式")
    private String corn;

    @ApiModelProperty(value = "标识")
    private String bean;

    @ApiModelProperty(value = "描述")
    private String memo;

    @ApiModelProperty(value = "状态")
    private ScheduledTaskStatus status;

    @ApiModelProperty(value = "上次执行日期")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime latestDate;

    @ApiModelProperty(value = "执行耗时(秒)")
    private Long timeConsuming;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private String creatorName;

    @ApiModelProperty(value = "最后修改时间")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    @ApiModelProperty("最后修改人")
    private String updaterName;


}
