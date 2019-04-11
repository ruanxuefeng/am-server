package com.am.server.api.admin.user.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 *
 * @author 阮雪峰
 * @date 2018/8/2 13:52
 */
@Builder(toBuilder = true)
@Data
@Document
public class UserPermissionCache {
    @Id
    private Long id;
    private List<String> permissions;
}
