package com.am.server.api.role.pojo.ao;

import com.am.server.common.base.pojo.ao.PageAo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * list
 * @author 阮雪峰
 * @date 2019年6月24日17:00:51
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class RoleListAo extends PageAo {
    @ApiModelProperty("角色名称")
    private String name;
}
