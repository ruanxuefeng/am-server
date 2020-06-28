package com.am.server.api.permission.pojo.po;

import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author 阮雪峰
 * @date 2020年6月23日09:44:05
 */
@Getter
@Setter
public class PermissionCollection {
    private TreeSet<PermissionTreeDo> set;

    private List<String> permissionList;

    public PermissionCollection() {
    }

    public PermissionCollection(TreeSet<PermissionTreeDo> set, List<String> permissionList) {
        this.set = set;
        this.permissionList = permissionList;
    }
}
