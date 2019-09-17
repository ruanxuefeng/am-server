package com.am.server.api.user.pojo.ao;

import com.am.server.common.base.enumerate.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

/**
 * 登录用户修改自己的信息
 * @author 阮雪峰
 * @date 2019年6月24日15:30:58
 */
@ApiModel
@Accessors(chain = true)
@Data
public class UpdateUserInfoAO {
    @ApiModelProperty(value = "主键", example = "1234567890", required = true)
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("性别")
    private Gender gender;

    @ApiModelProperty("头像链接")
    private String avatar;

    @ApiModelProperty("头像文件")
    private MultipartFile img;
}
