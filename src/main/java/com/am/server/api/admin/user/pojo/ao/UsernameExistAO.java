package com.am.server.api.admin.user.pojo.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户名是否被使用
 * @author 阮雪峰
 * @date 2019年6月24日09:34:28
 */
@ApiModel
@Data
public class UsernameExistAO {
    @ApiModelProperty(value = "用户id(在修改的时候必填)", example = "123456789")
    private Long id;
    @ApiModelProperty(value = "用户名", required = true)
    private String username;
}
