package com.am.server.api.task.pojo.ao;

import com.am.server.common.base.pojo.ao.PageAo;
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
public class ScheduledTaskListAo extends PageAo {
    @ApiModelProperty("任务名称")
    private String name;
}
