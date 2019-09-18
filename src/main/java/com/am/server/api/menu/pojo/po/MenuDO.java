package com.am.server.api.menu.pojo.po;

import com.am.server.api.role.pojo.po.RoleDO;
import com.am.server.api.user.pojo.po.AdminUserDO;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
public class MenuDO {
    @Id
    private Long id;

    private String name;

    private String mark;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "pid")
    private MenuDO parent;

    private Integer level;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @OrderBy("id desc")
    @JoinColumn(name = "pid")
    private List<MenuDO> children;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @OrderBy("id desc")
    @JoinTable(name = "role_menu",
            joinColumns = {@JoinColumn(name = "menu")},
            inverseJoinColumns = {@JoinColumn(name = "role")})
    private List<RoleDO> roles;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "created_by", updatable = false)
    private AdminUserDO createdBy;

    @Column(updatable = false)
    private LocalDateTime createdTime;
}
