package com.am.server.api.bulletin.pojo.ao;

import com.am.server.common.base.validator.Save;
import com.am.server.common.base.validator.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 阮雪峰
 * @date 2019年6月26日15:09:57
 */
@ApiModel
@Data
public class UpdateBulletinAo {
    @NotNull(message = "common.operate.primaryKey.null")
    @ApiModelProperty(value = "id", example = "1234567890")
    private Long id;

    /**
     * 标题
     */
    @NotBlank(message = "bulletin.title.blank")
    @Length(max = 100, message = "bulletin.title.tooLong")
    @ApiModelProperty(value = "标题")
    private String title;

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
