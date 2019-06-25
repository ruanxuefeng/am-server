package com.am.server.api.admin.menu.pojo.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author 阮雪峰
 * @date 2019年6月25日14:23:03
 */
@ApiModel
@Data
public class UpdateMenuAO {

    @ApiModelProperty(value = "id", example = "1234567890")
    private Long id;

    @ApiModelProperty("名称")
    @NotBlank(message = "menu.name.blank")
    @Length(min = 1, max = 20, message = "menu.name.length")
    private String name;

    @ApiModelProperty("Key")
    @NotBlank(message = "menu.key.blank")
    private String key;

    @ApiModelProperty(value = "pid", example = "1234567890")
    private Long pid;
}
