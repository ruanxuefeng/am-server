package com.am.server.common.util;

import java.util.regex.Pattern;

/**
 *
 * @author 阮雪峰
 * @date 2018/4/13 10:54
 */
public class MongoUtil {
    private MongoUtil() {
    }

    /**
     * 获取模糊查询正则
     * @param keyWord keyWord
     * @return java.util.regex.Pattern
     * @author 阮雪峰
     * @date 2018/4/13 10:59
     */
    public static Pattern getRegx(String keyWord) {
        String regex = "^.*" + keyWord + ".*$";
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }
}
