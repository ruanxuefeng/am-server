package com.am.server.api.admin.role.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色权限关联表
 * @author 阮雪峰
 * @date 2019/2/13 15:29
 */
@Data
@Entity
@Table(name = "role_menu")
public class RoleMenu {
    @Id
    private Long id;
    private Long role;
    private Long menu;

    public RoleMenu() {
    }

    public RoleMenu(Long id, Long role, Long menu) {
        this.id = id;
        this.role = role;
        this.menu = menu;
    }
}
