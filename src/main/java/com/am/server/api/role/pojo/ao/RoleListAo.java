package com.am.server.api.role.pojo.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * list
 * @author 阮雪峰
 * @date 2019年6月24日17:00:51
 */
@ApiModel
@Data
public class RoleListAo {
    @ApiModelProperty(value = "要查询页数", required = true, example = "1")
    private Integer page;

    @ApiModelProperty(value = "每页记录数", required = true, example = "20")
    private Integer pageSize;

    @ApiModelProperty("角色名称")
    private String name;
}
