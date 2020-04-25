package com.am.server.api.permission.pojo.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.TreeSet;

/**
 * 权限树
 *
 * @author 阮雪峰
 * @date 2019年9月18日16:54:34
 */
@Accessors(chain = true)
@Data
public class PermissionTreeDo implements Comparable<PermissionTreeDo> {
    private String name;
    private String mark;

    private Integer sort;
    private TreeSet<PermissionTreeDo> children;

    @Override
    public int compareTo(PermissionTreeDo o) {
        if (o == null || mark.equals(o.mark)) {
            return 0;
        } else if (sort.equals(o.sort)) {
            return mark.compareTo(o.mark);
        } else {
            return sort.compareTo(o.sort);
        }
    }
}
