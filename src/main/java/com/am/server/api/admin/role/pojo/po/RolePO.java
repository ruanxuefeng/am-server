package com.am.server.api.admin.role.pojo.po;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author 阮雪峰
 * @date 2019年6月25日09:18:55
 */
@Entity
@Table(name = "role")
@Accessors(chain = true)
@Data
public class RolePO {
    @javax.persistence.Id
    private Long id;

    @Column(updatable = false)
    private Long creator;

    @Column(length = 32)
    private String name;

    @Column(length = 256, name = "`describe`")
    private String describe;

    @Column(updatable = false)
    private LocalDateTime createTime;
}
