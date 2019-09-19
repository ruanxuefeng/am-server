package com.am.server.api.user.pojo.vo;

import com.am.server.common.base.enumerate.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

/**
 * 用户信息
 * @author 阮雪峰
 * @date 2019年6月24日14:26:40
 */
@ApiModel
@Accessors(chain = true)
@Data
public class UserInfoVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "性别")
    private Gender gender;

    @ApiModelProperty(value = "角色")
    private List<String> roles;

    @ApiModelProperty(value = "拥有的权限")
    private Set<String> permissions;
}
