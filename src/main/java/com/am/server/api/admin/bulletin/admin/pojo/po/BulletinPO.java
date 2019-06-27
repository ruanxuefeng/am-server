package com.am.server.api.admin.bulletin.admin.pojo.po;

import com.am.server.api.admin.bulletin.admin.pojo.Status;
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
public class BulletinPO {

    @javax.persistence.Id
    private Long id;

    @Column(updatable = false)
    private Long creator;

    @Column(updatable = false)
    private LocalDateTime createTime;

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
}
