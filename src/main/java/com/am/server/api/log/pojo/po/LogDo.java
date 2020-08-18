package com.am.server.api.log.pojo.po;

import com.am.server.common.base.pojo.po.BaseDo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 阮雪峰
 * @date 2019年6月25日15:23:41
 */
@Entity
@Table(name = "log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class LogDo extends BaseDo {

    /**
     * 操作人
     */
    @Column(name = "name")
    private String name;

    /**
     * 菜单
     */
    @Column(name = "menu")
    private String menu;

    /**
     * 操作
     */
    @Column(name = "operate")
    private String operate;

    /**
     * ip地址
     */
    @Column(name = "ip")
    private String ip;

}


