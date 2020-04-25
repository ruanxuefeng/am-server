package com.am.server.api.bulletin.pojo.po;

import com.am.server.api.bulletin.pojo.Status;
import com.am.server.common.base.pojo.po.BaseDo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author 阮雪峰
 * @date 2019年6月26日14:49:45
 */
@Accessors(chain = true)
@Setter
@Getter
@Entity
@Table(name = "bulletin")
public class BulletinDo extends BaseDo {

    /**
     * 内容
     */
    @Column(length = 256)
    private String content;

    /**
     * 发布时间
     */
    @Column
    private LocalDate date;

    /**
     * 状态
     */
    @Enumerated(EnumType.ORDINAL)
    @Column
    private Status status;

    /**
     * 过期天数
     */
    private Integer days;

    @Override
    public String toString() {
        return "BulletinDO{" +
                "id=" + getId() +
                "content='" + content + '\'' +
                ", date=" + date +
                ", status=" + status +
                ", days=" + days +
                '}';
    }
}
