package com.am.server.api.role.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 下拉选择的数据
 * @author 阮雪峰
 * @date 2019年6月25日09:36:52
 */
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
@Data
public class SelectRoleVO {
    @ApiModelProperty(value = "id", example = "1234567890")
    private Long id;

    @ApiModelProperty(value = "名称",example = "管理员")
    private String name;
}
