package com.am.server.api.user.pojo.ao;

import com.am.server.common.base.validator.Login;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录
 * @author 阮雪峰
 * @date 2019年6月20日15:06:38
 */
@ApiModel
@Data
public class LoginAO {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "login.username.blank", groups = {Login.class})
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "login.password.blank", groups = {Login.class})
    private String password;
}
