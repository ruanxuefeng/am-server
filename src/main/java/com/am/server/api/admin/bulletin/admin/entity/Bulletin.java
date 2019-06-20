package com.am.server.api.admin.bulletin.admin.entity;

import com.am.server.common.base.entity.BaseEntity;
import com.am.server.common.base.validator.Delete;
import com.am.server.common.base.validator.Id;
import com.am.server.common.base.validator.Save;
import com.am.server.common.base.validator.Update;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author 阮雪峰
 * @date 2018/11/13 10:56
 */
@EqualsAndHashCode()
@Data
@Entity
@Table(name = "bulletin")
public class Bulletin implements BaseEntity {

    @javax.persistence.Id
    @NotNull(message = "common.delete.primaryKey.null", groups = {Delete.class})
    @NotNull(message = "common.operate.primaryKey.null", groups = {Id.class})
    private Long id;

    @Column(updatable = false)
    private Long creator;
    @Transient
    private String userName;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createTime;
    /**
     * 内容
     */
    @NotBlank(message = "bulletin.content.blank", groups = {Save.class, Update.class})
    @Length(max = 200, message = "bulletin.content.tooLong", groups = {Save.class, Update.class})
    @Column(length = 256)
    private String content;

    /**
     * 发布时间
     */
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd")
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

    public Bulletin() {
    }

    public Bulletin(Long id) {
        this.id = id;
    }
}
