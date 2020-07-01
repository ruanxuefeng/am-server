package com.am.server.api.user.pojo.ao;

import com.am.server.common.base.enumerate.Gender;
import com.am.server.common.constant.RegularConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 保存、修改
 *
 * @author 阮雪峰
 * @date 2019年6月20日16:17:21
 */
@ApiModel
@Data
public class UpdateAdminUserAo {
    @ApiModelProperty(value = "主键", example = "1234567890", required = true)
    @NotNull(message = "common.operate.primaryKey.null")
    private Long id;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "login.username.blank")
    @Length(max = 64, message = "user.username.long")
    private String username;

    @NotBlank(message = "user.name.blank")
    @Length(max = 64, message = "user.name.long")
    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("邮箱")
    @NotBlank(message = "user.email.blank")
    @Email(regexp = RegularConstant.EMAIL, message = "user.email.format")
    private String email;

    @ApiModelProperty("性别")
    private Gender gender;

    @ApiModelProperty("头像链接")
    private String avatar;

    @ApiModelProperty("头像文件")
    private MultipartFile img;
}
