package com.am.server.api.role.pojo.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 阮雪峰
 * @date 2019年9月19日10:39:38
 */
@Data
@Accessors(chain = true)
public class UpdateRolePermissionAo {
    @ApiModelProperty(value = "id", example = "1234567890")
    @NotNull(message = "common.operate.primaryKey.null")
    private Long id;

    @ApiModelProperty(value = "permissions", example = "system, system-user")
    private List<String> permissions;
}
