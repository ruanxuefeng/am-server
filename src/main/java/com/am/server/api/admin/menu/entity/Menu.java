package com.am.server.api.admin.menu.entity;

import com.am.server.common.base.entity.BaseEntity;
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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 *  菜单
 * @author 阮雪峰
 * @date 2018/7/30 14:54
 */
@EqualsAndHashCode
@Entity
@Table(name = "menu")
@Data
public class Menu implements BaseEntity {

    @javax.persistence.Id
    @NotNull(message = "common.delete.primaryKey.null", groups = {Delete.class})
    @NotNull(message = "common.operate.primaryKey.null", groups = {Id.class})
    private Long id;

    @Column(updatable = false)
    private Long creator;
    @Transient
    private String userName;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createTime;

    @NotBlank(message = "menu.name.blank", groups = {Save.class, Update.class})
    @Length(min = 1, max = 20, message = "menu.name.length", groups = {Save.class, Update.class})
    @Column(length = 32)
    private String name;

    @NotBlank(message = "menu.key.blank", groups = {Save.class, Update.class})
    @Column(length = 32, name = "`key`")
    private String key;

    @Column(length = 18)
    private Long pid;
    @Transient
    private String parentName;

    private Integer level;

    @OneToMany(fetch = FetchType.EAGER)
    @OrderBy("id desc")
    @JoinColumn(name = "pid", insertable = false, updatable = false)
    private List<Menu> children;

    public Menu() {
    }

    public Menu(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Menu(Long id, String name, List<Menu> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }
}
