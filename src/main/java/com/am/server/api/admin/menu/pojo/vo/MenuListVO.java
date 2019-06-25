package com.am.server.api.admin.menu.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author 阮雪峰
 * @date 2019年6月25日13:31:10
 */
@Accessors(chain = true)
@Data
public class MenuListVO {
    @ApiModelProperty(value = "id", example = "1234567890")
    private Long id;

    @ApiModelProperty("创建人")
    private String creatName;

    @ApiModelProperty("创建时间")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("Key")
    private String key;

    @ApiModelProperty(value = "父级id", example = "1234567890")
    private Long pid;

    @ApiModelProperty("父级名称")
    private String parentName;
}
