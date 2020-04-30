package com.am.server.api.task.pojo.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 阮雪峰
 * @date 2020年4月26日21:25:03
 */
@ApiModel
@Setter
@Getter
public class UpdateScheduledTaskAo {
    @NotNull(message = "common.operate.primaryKey.null")
    @ApiModelProperty(value = "主键",example = "123456789")
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "scheduled.task.name.blank")
    @ApiModelProperty("名称")
    private String name;

    /**
     * corn表达式
     */
    @NotBlank(message = "scheduled.task.corn.blank")
    @ApiModelProperty("corn表达式")
    private String corn;

    /**
     * 标识
     */
    @NotBlank(message = "scheduled.task.bean.blank")
    @ApiModelProperty("标识")
    private String bean;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String memo;
}
