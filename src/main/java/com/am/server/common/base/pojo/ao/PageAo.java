package com.am.server.common.base.pojo.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 阮雪峰
 */
@Setter
@Getter
public class PageAo {

    @ApiModelProperty(value = "要查询页数", required = true, example = "1")
    private Integer page;

    @ApiModelProperty(value = "每页记录数", required = true, example = "20")
    private Integer pageSize;
}
