package com.am.server.api.log.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author 阮雪峰
 * @date 2019年6月25日15:34:34
 */
@ApiModel
@Accessors(chain = true)
@Data
public class LogListVo {

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String name;

    /**
     * 菜单
     */
    @ApiModelProperty("菜单")
    private String menu;

    /**
     * 操作
     */
    @ApiModelProperty("操作")
    private String operate;

    /**
     * ip地址
     */
    @ApiModelProperty("ip地址")
    private String ip;

    @ApiModelProperty("操作时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
