package com.am.server.api.bulletin.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author 阮雪峰
 */
@Accessors(chain = true)
@Setter
@Getter
public class BulletinContentVo {

    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;
}
