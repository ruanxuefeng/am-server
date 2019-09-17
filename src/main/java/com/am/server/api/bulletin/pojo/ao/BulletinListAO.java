package com.am.server.api.bulletin.pojo.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 阮雪峰
 * @date 2019年6月26日14:54:09
 */
@ApiModel
@Data
public class BulletinListAO {
    @ApiModelProperty(value = "要查询页数", required = true, example = "1")
    private Integer page;

    @ApiModelProperty(value = "每页记录数", required = true, example = "20")
    private Integer pageSize;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

}
