package com.am.server.api.admin.menu.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 阮雪峰
 * @date 2019年6月25日14:40:44
 */
@ApiModel
@Accessors(chain = true)
@Data
public class ParentMenuVO {
    @ApiModelProperty(value = "id", example = "1234567890")
    private Long id;

    @ApiModelProperty("名称")
    private String name;
}
