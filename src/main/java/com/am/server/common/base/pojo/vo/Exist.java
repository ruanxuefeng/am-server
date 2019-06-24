package com.am.server.common.base.pojo.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 判断是否重复时使用的实体
 * @author 阮雪峰
 * @date 2019年6月24日09:39:29
 */
@Api("判断是否重复或者存在时使用的实体")
@AllArgsConstructor
@Data
public class Exist {
    @ApiModelProperty("true or false")
    boolean exist;
}
