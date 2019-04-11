package com.am.server.common.base.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *  分页
 * @author 阮雪峰
 * @date 2018/7/26 14:40
 */
@Data
public class Page<T> implements Serializable {
    /**
     * 当前页
     */
    private int page = 1;
    /**
     * 每页记录数
     */
    private int pageSize = 20;
    /**
     * 总记录数
     */
    private int total;

    /**
     * 查询的数据
     */
    private List<T> rows;

    /**
     * 获取总页数
     * @return int
     */
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
