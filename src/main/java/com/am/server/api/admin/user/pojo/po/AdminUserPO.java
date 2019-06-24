package com.am.server.api.admin.user.pojo.po;

import com.am.server.api.admin.role.entity.Role;
import com.am.server.common.base.enumerate.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * admin_user
 *
 * @author 阮雪峰
 * @date 2019年6月20日15:03:11
 */
@Table(name = "admin_user")
@Entity
@EqualsAndHashCode(exclude = {"id"})
@Accessors(chain = true)
@Data
public class AdminUserPO {
    @javax.persistence.Id
    @Column(length = 18)
    private Long id;

    @Column(length = 18, updatable = false)
    private Long creator;

    @Column(length = 64)
    private String username;

    @Column(length = 64)
    private String email;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Column(length = 64, updatable = false)
    private String password;

    @Column(length = 512, name = "`key`", updatable = false)
    private String key;

    @Column(length = 512)
    private String avatar;

    @Column(length = 128)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @OrderBy("id desc")
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user", insertable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "role", insertable = false, updatable = false)})
    private List<Role> roleList;
}
