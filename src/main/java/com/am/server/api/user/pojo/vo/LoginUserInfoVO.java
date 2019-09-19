package com.am.server.api.user.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 登录用户
 * @author 阮雪峰
 * @date 2019年6月24日13:45:42
 */
@ApiModel
@AllArgsConstructor
@Data
public class LoginUserInfoVO {
    @ApiModelProperty(value = "登录凭证", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3MTkzNDI5Nzk4MjY4NDc3NDQiLCJpYXQiOjE1NjEzNTUxNjksImV4cCI6MTU2MTk1OTk2OSwiaXNzIjoiYWQtc2VydmVyIn0.-LYVjC6-8zd8SnZ4iasu5BDsqsh9v2a0UyPYs5uAuPw")
    private String token;
}
