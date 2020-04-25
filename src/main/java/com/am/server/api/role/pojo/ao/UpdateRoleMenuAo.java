package com.am.server.api.role.pojo.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 修改角色权限
 *
 * @author 阮雪峰
 * @date 2019年6月25日09:49:38
 */
@ApiModel
@Data
public class UpdateRoleMenuAo {
    @ApiModelProperty(value = "id", example = "1234567890")
    @NotNull(message = "common.operate.primaryKey.null")
    private Long id;

    @ApiModelProperty(value = "权限id数组", example = "[1,2,3,4]")
    private List<Long> menuList;
}
