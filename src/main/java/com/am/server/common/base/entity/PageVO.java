package com.am.server.common.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 *  分页
 * @author 阮雪峰
 * @date 2018/7/26 14:40
 */
@ApiModel("分页实体")
@Accessors(chain = true)
@Data
public class PageVO<T> implements Serializable {
    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页", example = "1")
    private Integer page;
    /**
     * 每页记录数
     */
    @ApiModelProperty(value = "每页记录数", example = "20")
    private Integer pageSize = 20;
    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数", example = "10")
    private Integer total;

    /**
     * 查询的数据
     */
    @ApiModelProperty(value = "查询的数据")
    private List<T> rows;

    /**
     * 获取总页数
     * @return int
     */
    @ApiModelProperty(value = "当前页", example = "200")
    public int getTotalPage() {
        return (total % pageSize == 0 ? (total / pageSize) : (total / pageSize + 1));
    }

    /**
     * 获取开始查询的记录数
     * @return int
     */
    @JsonIgnore
    public int getCol() {
        return (page - 1) * pageSize;
    }

}
