package com.am.server.api.admin.bulletin.admin.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 阮雪峰
 */
@AllArgsConstructor
@Data
public class BulletinContentVO {
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;
}
