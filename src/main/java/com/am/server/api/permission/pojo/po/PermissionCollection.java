package com.am.server.api.permission.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.TreeSet;

/**
 * @author 阮雪峰
 * @date 2020年6月23日09:44:05
 */
@AllArgsConstructor
@Data
public class PermissionCollection {
    private TreeSet<PermissionTreeDo> set;
}
