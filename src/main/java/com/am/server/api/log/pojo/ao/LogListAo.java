package com.am.server.api.log.pojo.ao;

import com.am.server.common.base.pojo.ao.PageAo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author 阮雪峰
 * @date 2019年6月25日15:29:19
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Accessors(chain = true)
@Data
public class LogListAo extends PageAo {
    @ApiModelProperty(value = "操作人姓名")
    private String name;

    @ApiModelProperty(value = "操作")
    private String operate;

    @ApiModelProperty(value = "菜单名称")
    private String menu;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDate endDate;
}
