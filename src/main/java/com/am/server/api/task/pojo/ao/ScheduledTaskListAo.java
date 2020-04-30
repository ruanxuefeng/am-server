package com.am.server.api.task.pojo.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 列表查询实体类
 * @author 阮雪峰
 * @date 2020年4月26日21:16:43
 */
@ApiModel
@Setter
@Getter
public class ScheduledTaskListAo {
    @ApiModelProperty(value = "要查询页数", required = true, example = "1")
    private Integer page;

    @ApiModelProperty(value = "每页记录数", required = true, example = "20")
    private Integer pageSize;

    @ApiModelProperty("任务名称")
    private String name;
}
