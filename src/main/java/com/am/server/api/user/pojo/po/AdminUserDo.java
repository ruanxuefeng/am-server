package com.am.server.api.user.pojo.po;

import com.am.server.api.role.pojo.po.RoleDo;
import com.am.server.api.upload.pojo.po.SysFileDo;
import com.am.server.common.base.enumerate.Gender;
import com.am.server.common.base.pojo.po.BaseDo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

/**
 * admin_user
 *
 * @author 阮雪峰
 * @date 2019年6月20日15:03:11
 */
@Table(name = "admin_user")
@Entity
@Accessors(chain = true)
@ToString(of = {"username", "email"})
@Setter
@Getter
public class AdminUserDo extends BaseDo {
    @Column
    private String username;

    @Column
    private String email;

    @Column(updatable = false)
    private String password;

    @Column(updatable = false)
    private String salt;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "avatar", referencedColumnName = "id")
    private SysFileDo avatar;

    @Column
    private String name;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @OrderBy("id desc")
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user")},
            inverseJoinColumns = {@JoinColumn(name = "role")})
    private List<RoleDo> roles;
}
