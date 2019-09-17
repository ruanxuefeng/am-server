package com.am.server.api.user.pojo.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 邮箱是否被使用
 * @author 阮雪峰
 * @date 2019年6月24日09:22:25
 */
@ApiModel
@Data
public class EmailExistAO {
    @ApiModelProperty(value = "用户id(在修改的时候必填)", example = "1234567890")
    private Long id;
    @ApiModelProperty(value = "用户邮箱", required = true)
    private String email;
}
