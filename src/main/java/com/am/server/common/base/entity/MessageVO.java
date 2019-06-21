package com.am.server.common.base.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 返回消息
 * @author 阮雪峰
 */
@ApiModel
@Data
public class MessageVO {

    @ApiModelProperty("消息内容")
    private String message;

}
