package com.am.server.api.admin.user.pojo.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 修改角色
 *
 * @author 阮雪峰
 * @date 2019年6月24日09:07:05
 */
@ApiModel
@Data
public class UpdateRoleAO {
    @ApiModelProperty(value = "用户主键", example = "1234567890987654321", required = true)
    private Long id;
    @ApiModelProperty(value = "角色主键数组", example = "[1234567890987654321,1234567890987654321]")
    private List<Long> roleIdList;
}
