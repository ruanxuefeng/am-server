package com.am.server.api.admin.bulletin.admin.pojo.vo;

import com.am.server.api.admin.bulletin.admin.pojo.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 阮雪峰
 * @date 2019年6月26日14:59:38
 */
@ApiModel
@Accessors(chain = true)
@Data
public class BulletinListVO {

    @ApiModelProperty(value = "id", example = "1234567890")
    private Long id;

    @ApiModelProperty(value = "创建人")
    private String creatorName;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 发布时间
     */
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "发布时间")
    private LocalDate date;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", example = "发布/未发布/过期")
    private Status status;

    /**
     * 过期天数
     */
    @ApiModelProperty(value = "days", example = "10")
    private Integer days;
}
