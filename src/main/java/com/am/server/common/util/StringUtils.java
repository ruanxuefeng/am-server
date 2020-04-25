package com.am.server.common.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 *  字符串工具类
 * @author 阮雪峰
 * @date 2018/8/1 13:57
 */
public class StringUtils extends org.springframework.util.StringUtils {
    private final static String CHARS = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890";
    private final static String NUM = "1234567890";

    private StringUtils() {
    }

    /**
     * 获取随机数
     * @param length 长度
     * @return java.lang.Long
     * @author 阮雪峰
     * @date 2018/9/5 9:29
     */
    public static Long getRandomNumber(int length) {
        return Long.valueOf(getRandomNumberStr(length));
    }

    /**
     * 获取随机数
     * @param length 长度
     * @return java.lang.String
     * @author 阮雪峰
     * @date 2018/9/5 9:27
     */
    public static String getRandomNumberStr(int length) {
        return RandomStringUtils.random(length, NUM);
    }

    /**
     * 获取随机字符串
     * @param length 随机字符串长度
     * @return java.lang.String
     * @author 阮雪峰
     * @date 2018/9/5 9:26
     */

    public static String getRandomStr(int length) {
        return RandomStringUtils.random(length, CHARS);
    }

}
