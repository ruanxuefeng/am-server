package com.am.server.api.role.pojo.po;


import com.am.server.api.menu.pojo.po.MenuDO;
import com.am.server.api.user.pojo.po.AdminUserDO;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 阮雪峰
 * @date 2019年6月25日09:18:55
 */
@Entity
@Table(name = "role")
@Accessors(chain = true)
@Data
public class RoleDO {
    @javax.persistence.Id
    private Long id;

    private String name;

    private String memo;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @OrderBy("id desc")
    @JoinTable(name = "role_menu",
            joinColumns = {@JoinColumn(name = "role")},
            inverseJoinColumns = {@JoinColumn(name = "menu")})
    private List<MenuDO> menus;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @OrderBy("id desc")
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "role")},
            inverseJoinColumns = {@JoinColumn(name = "user")})
    private List<AdminUserDO> users;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "created_by", updatable = false)
    private AdminUserDO createdBy;

    @Column(updatable = false)
    private LocalDateTime createdTime;
}
