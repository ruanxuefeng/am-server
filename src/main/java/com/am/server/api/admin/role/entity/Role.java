package com.am.server.api.admin.role.entity;

import com.am.server.common.base.pojo.BaseEntity;
import com.am.server.common.base.validator.Delete;
import com.am.server.common.base.validator.Id;
import com.am.server.common.base.validator.Save;
import com.am.server.common.base.validator.Update;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 *  角色
 * @author 阮雪峰
 * @date 2018/7/27 10:50
 */
@EqualsAndHashCode()
@Entity
@Table(name = "role")
@Data
public class Role implements BaseEntity {

    @javax.persistence.Id
    @NotNull(message = "common.delete.primaryKey.null", groups = {Delete.class})
    @NotNull(message = "common.operate.primaryKey.null", groups = {Id.class})
    private Long id;

    @Column(updatable = false)
    private Long creator;
    @Transient
    private String userName;

    @NotBlank(message = "role.name.blank", groups = {Save.class, Update.class})
    @Length(min = 1, max = 20, message = "role.name.length", groups = {Save.class, Update.class})
    @Column(length = 32)
    private String name;

    @Size(max = 200, message = "role.describe.long", groups = {Save.class, Update.class})
    @Column(length = 256, name = "`describe`")
    private String describe;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createTime;

    @Transient
    private List<Long> menuList;


    public Role() {
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
