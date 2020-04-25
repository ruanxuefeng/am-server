package com.am.server.common.base.pojo.po;

import com.am.server.api.user.pojo.po.AdminUserDo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 所有DO实体的父类
 *
 * @author 阮雪峰
 * @date 2020年4月25日10:42:58
 */
@Accessors(chain = true)
@Getter
@Setter
@MappedSuperclass
public class BaseDo {
    /**
     * 主键
     */
    @Id
    private Long id;

    @Column(name = "created_by")
    private Long createdById;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    private AdminUserDo createdBy;

    @Column(updatable = false)
    private LocalDateTime createdTime;

    @Column(name = "updated_by")
    private Long updatedById;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "updated_by", insertable = false, updatable = false)
    private AdminUserDo updatedBy;

    @Column
    private LocalDateTime updatedTime;

    @Version
    private Long revision;
}


