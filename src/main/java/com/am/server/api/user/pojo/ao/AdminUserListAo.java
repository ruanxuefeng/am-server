package com.am.server.api.user.pojo.ao;

import com.am.server.common.base.pojo.ao.PageAo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 列表查询条件
 *
 * @author 阮雪峰
 * @date 2019年6月20日15:56:36
 */
@ApiModel
@Setter
@Getter
public class AdminUserListAo extends PageAo {

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("邮箱")
    private String email;
}
