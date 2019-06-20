package com.am.server.api.admin.user.pojo.vo;

import com.am.server.common.base.enumerate.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * @author 阮雪峰
 * @date 2019年6月20日20:20:37
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
@Data
public class AdminUserListVO {
    private Long id;
    private String creatorName;
    private String username;
    private String email;
    private String avatar;
    private String name;
    private Gender gender;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
