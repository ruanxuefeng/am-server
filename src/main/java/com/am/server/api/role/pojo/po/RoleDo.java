package com.am.server.api.role.pojo.po;


import com.am.server.api.user.pojo.po.AdminUserDo;
import com.am.server.common.base.pojo.po.BaseDo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

/**
 * @author 阮雪峰
 * @date 2019年6月25日09:18:55
 */
@Entity
@Table(name = "role")
@Accessors(chain = true)
@Getter
@Setter
public class RoleDo extends BaseDo {
    private String name;

    private String memo;

    @ElementCollection
    @CollectionTable(name = "role_permission", joinColumns = @JoinColumn(name = "role"))
    @Column(name = "permission")
    private List<String> permissions;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @OrderBy("id desc")
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "role")},
            inverseJoinColumns = {@JoinColumn(name = "user")})
    private List<AdminUserDo> users;

    @Override
    public String toString() {
        return "RoleDO{" +
                "id=" + getId() +
                "name='" + name + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }
}
