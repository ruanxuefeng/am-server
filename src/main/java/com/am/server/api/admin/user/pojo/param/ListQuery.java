package com.am.server.api.admin.user.pojo.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 列表查询条件
 *
 * @author 阮雪峰
 * @date 2019年6月20日15:56:36
 */
@ApiModel
@Data
public class ListQuery {

    @ApiModelProperty("要查询页数")
    private Integer page;

    @ApiModelProperty("每页记录数")
    private Integer pageSize;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("邮箱")
    private String email;
}
