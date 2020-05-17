package com.am.server.api.bulletin.pojo.ao;

import com.am.server.common.base.pojo.ao.PageAo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 阮雪峰
 * @date 2019年6月26日14:54:09
 */
@ApiModel
@Setter
@Getter
public class BulletinListAo extends PageAo {
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

}
