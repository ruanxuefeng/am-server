package com.am.server.api.admin.menu.pojo.po;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单
 * @author 阮雪峰
 * @date 2019年6月25日13:26:14
 */
@Entity
@Table(name = "menu")
@Accessors(chain = true)
@Data
public class MenuPO {
    @javax.persistence.Id
    private Long id;

    @Column(updatable = false)
    private Long creator;

    @Column(updatable = false)
    private LocalDateTime createTime;

    @Column(length = 32)
    private String name;

    @Column(length = 32, name = "`key`")
    private String key;

    @Column(length = 18)
    private Long pid;

    private Integer level;

    @OneToMany(fetch = FetchType.EAGER)
    @OrderBy("id desc")
    @JoinColumn(name = "pid", insertable = false, updatable = false)
    private List<MenuPO> children;
}
