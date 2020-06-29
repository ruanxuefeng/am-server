package com.am.server.common.base.enumerate;

/**
 * 性别
 *
 * @author 阮雪峰
 * @date 2018/7/24 9:57
 */
public enum Gender {
    /**
     * 男
     */
    MALE("男"),
    /**
     * 女
     */
    FEMALE("女");

    Gender(String value) {
        this.value = value;
    }

    private final String value;
}
