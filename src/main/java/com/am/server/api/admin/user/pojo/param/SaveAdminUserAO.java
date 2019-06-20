package com.am.server.api.admin.user.pojo.param;

import com.am.server.common.base.enumerate.Gender;
import com.am.server.common.base.validator.Save;
import com.am.server.common.base.validator.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 保存、修改
 *
 * @author 阮雪峰
 * @date 2019年6月20日16:17:21
 */
@ApiModel
@Data
public class SaveAdminUserAO {

    @ApiModelProperty("用户名")
    @NotBlank(message = "login.username.blank", groups = {Save.class, Update.class})
    @Length(max = 64, message = "user.username.long", groups = {Save.class, Update.class})
    private String username;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("邮箱")
    @NotBlank(message = "login.email.blank", groups = {Save.class, Update.class})
    @Email(message = "login.email.error", groups = {Save.class, Update.class})
    private String email;

    @ApiModelProperty("性别")
    private Gender gender;

    @ApiModelProperty("头像")
    private MultipartFile img;
}
