package com.am.server.common.base.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 返回消息
 * @author 阮雪峰
 */
@ApiModel
@AllArgsConstructor
@Data
public class MessageVO {

    @ApiModelProperty("消息内容")
    private String message;

}
