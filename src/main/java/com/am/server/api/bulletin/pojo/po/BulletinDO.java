package com.am.server.api.bulletin.pojo.po;

import com.am.server.api.bulletin.pojo.Status;
import com.am.server.api.user.pojo.po.AdminUserDO;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 阮雪峰
 * @date 2019年6月26日14:49:45
 */
@Accessors(chain = true)
@Data
@Entity
@Table(name = "bulletin")
public class BulletinDO {

    @Id
    private Long id;

    /**
     * 内容
     */
    @Column(length = 256)
    private String content;

    /**
     * 发布时间
     */
    @Column(updatable = false)
    private LocalDate date;

    /**
     * 状态
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(updatable = false)
    private Status status;

    /**
     * 过期天数
     */
    private int days;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "created_by", updatable = false)
    private AdminUserDO createdBy;

    @Column(updatable = false)
    private LocalDateTime createdTime;
}
