package com.am.server.api.role.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author 阮雪峰
 * @date 2019年9月19日10:27:38
 */
@Data
@Accessors(chain = true)
public class PermissionTreeVo {
    private String name;
    private String mark;
    private List<PermissionTreeVo> children;
}
