package com.am.server.api.admin.bulletin.admin.pojo;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 公告状态
 * @author 阮雪峰
 * @date 2018/11/13 12:59
 */
public enum Status {
    /**
     * 未发布
     */
    UNPUBLISHED("未发布"),
    /**
     * 已发布
     */
    PUBLISHED("已发布"),
    /**
     * 过期
     */
    EXPIRED("过期");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
