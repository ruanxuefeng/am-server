package com.am.server.api.permission.pojo.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 权限树
 *
 * @author 阮雪峰
 * @date 2019年9月18日16:54:34
 */
@Accessors(chain = true)
@Data
public class PermissionTreeDO {
    private String name;
    private String mark;

    private List<PermissionTreeDO> children;
}
