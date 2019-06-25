package com.am.server.api.admin.role.pojo.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author 阮雪峰
 * @date 2019年6月25日08:44:02
 */
@ApiModel
@Data
public class SaveRoleAO {

    @NotBlank(message = "role.name.blank")
    @Length(min = 1, max = 20, message = "role.name.length")
    @ApiModelProperty(value = "名称",example = "管理员")
    private String name;

    @ApiModelProperty("备注")
    private String describe;
}
