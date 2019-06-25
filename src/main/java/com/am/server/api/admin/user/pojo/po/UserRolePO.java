package com.am.server.api.admin.user.pojo.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 用户角色关联表
 * @author 阮雪峰
 * @date 2019/1/16 12:46
 */
@Entity(name = "user_role")
@Data
public class UserRolePO {
    @Id
    private Long id;
    private Long user;
    private Long role;

    public UserRolePO() {
    }

    public UserRolePO(Long id, Long user, Long role) {
        this.id = id;
        this.user = user;
        this.role = role;
    }
}