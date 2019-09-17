package com.am.server.api.menu.pojo.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 列表查询
 * @author 阮雪峰
 * @date 2019年6月25日13:28:35
 */
@ApiModel
@Data
public class MenuListAO {
    @ApiModelProperty(value = "要查询页数", required = true, example = "1")
    private Integer page;

    @ApiModelProperty(value = "每页记录数", required = true, example = "20")
    private Integer pageSize;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("标识")
    private String mark;
}
