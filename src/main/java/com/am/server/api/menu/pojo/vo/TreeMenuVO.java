package com.am.server.api.menu.pojo.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author 阮雪峰
 * @date 2019年6月25日14:44:29
 */
@Api
@Accessors(chain = true)
@Data
public class TreeMenuVO {
    @ApiModelProperty(value = "id", example = "1234567890")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("子菜单")
    private List<TreeMenuVO> children;
}
