package com.am.server.api.user.pojo.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 *
 * @author 阮雪峰
 * @date 2018/8/2 13:52
 */
@Data
@Accessors(chain = true)
public class UserPermissionDo {
    private Long id;
    private Set<String> permissions;
}
