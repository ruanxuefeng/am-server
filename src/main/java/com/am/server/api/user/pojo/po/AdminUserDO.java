package com.am.server.api.user.pojo.po;

import com.am.server.api.role.pojo.po.RoleDO;
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
public class AdminUserDO {
    @javax.persistence.Id
    @Column
    private Long id;

    @Column
    private String username;

    @Column
    private String email;

    @Column(updatable = false)
    private String password;

    @Column(updatable = false)
    private String salt;

    @Column
    private String avatar;

    @Column
    private String name;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @OrderBy("id desc")
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user", insertable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "role", insertable = false, updatable = false)})
    private List<RoleDO> roles;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "created_by", updatable = false)
    private AdminUserDO createdBy;

    @Column(updatable = false)
    private LocalDateTime createdTime;
}
