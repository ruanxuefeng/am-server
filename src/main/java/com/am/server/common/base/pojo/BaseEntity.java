package com.am.server.common.base.pojo;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *  实体bean的基类
 * @author 阮雪峰
 * @date 2018/6/28 13:26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface BaseEntity extends Serializable {

    /**
     * 主键赋值
     * @param id id
     * @author 阮雪峰
     * @date 2019/2/18 13:29
     */
    void setId(Long id);

    /**
     * 创建时间赋值
     * @param createTime createTime
     * @author 阮雪峰
     * @date 2019/2/18 13:29
     */
    void setCreateTime(LocalDateTime createTime);

    /**
     * 创建人赋值
     * @param uid uid
     * @author 阮雪峰
     * @date 2019/2/18 13:29
     */
    default void setCreator(Long uid){}
}
