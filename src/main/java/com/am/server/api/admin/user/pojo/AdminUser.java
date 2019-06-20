package com.am.server.api.admin.user.pojo;

import com.am.server.api.admin.role.entity.Role;
import com.am.server.common.base.entity.BaseEntity;
import com.am.server.common.base.enumerate.Gender;
import com.am.server.common.base.validator.Delete;
import com.am.server.common.base.validator.Id;
import com.am.server.common.base.validator.Login;
import com.am.server.common.base.validator.Save;
import com.am.server.common.base.validator.Update;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户
 *
 * @author 阮雪峰
 * @date 2018/7/24 9:53
 */
@Table(name = "admin_user")
@Entity
@EqualsAndHashCode(exclude = {"id"})
@Accessors(chain = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUser {

    @NotNull(message = "common.delete.primaryKey.null", groups = {Delete.class})
    @NotNull(message = "common.operate.primaryKey.null", groups = {Id.class})
    @javax.persistence.Id
    @Column(length = 18)
    private Long id;

    @Column(length = 18, updatable = false)
    private Long creator;
    @Transient
    private String creatorName;

    @NotBlank(message = "login.username.blank", groups = {Login.class, Save.class, Update.class})
    @Length(max = 64, message = "user.username.long", groups = {Save.class, Update.class})
    @Column(length = 64)
    private String username;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @NotBlank(message = "login.email.blank", groups = {Save.class, Update.class})
    @Email(message = "login.email.error", groups = {Save.class, Update.class})
    @Column(length = 64)
    private String email;

    /**
     * JsonProperty.Access.WRITE_ONLY 在序列化的时候忽略字段，反序列化的时候不忽略 JSonIgnore无论是序列化还是反序列化都忽略
     */
    @NotBlank(message = "login.password.blank", groups = {Login.class})
    @Column(length = 64, updatable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    @Column(length = 512, name = "`key`", updatable = false)
    private String key;

    @Column(length = 512)
    private String avatar;

    @Column(length = 128)
    private String name;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Transient
    private String token;

    @Transient
    private List<String> menuList;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @OrderBy("id desc")
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user", insertable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "role", insertable = false, updatable = false)})
    private List<Role> roleList;

    @Transient
    private List<Long> roleIdList;
}
