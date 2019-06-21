package com.am.server.api.admin.user.pojo.vo;

import com.am.server.common.base.enumerate.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * @author 阮雪峰
 * @date 2019年6月20日20:20:37
 */
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
@Data
public class AdminUserListVO {
    @ApiModelProperty(value = "主键", example = "1234567890")
    private Long id;

    @ApiModelProperty("创建人")
    private String creatorName;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("性别")
    private Gender gender;

    @ApiModelProperty("创建时间")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
