package com.am.server.api.admin.bulletin.admin.pojo.ao;

import com.am.server.common.base.validator.Save;
import com.am.server.common.base.validator.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @author 阮雪峰
 * @date 2019年6月26日15:05:20
 */
@ApiModel
@Data
public class SaveBulletinAO {
    /**
     * 内容
     */
    @NotBlank(message = "bulletin.content.blank", groups = {Save.class, Update.class})
    @Length(max = 200, message = "bulletin.content.tooLong", groups = {Save.class, Update.class})
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 过期天数
     */
    @Min(value = 1, message = "bulletin.days.min")
    @ApiModelProperty(value = "days", example = "10")
    private Integer days;
}