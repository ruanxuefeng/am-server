package com.am.server.common.util

import org.apache.commons.lang3.RandomStringUtils

/**
 * 字符串工具类
 * @author 阮雪峰
 * @date 2019年6月20日13:29:24
 */
object StringUtils {

    private const val CHARS = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890"
    private const val NUM = "1234567890"
    /**
     * 获取随机数
     * @param length 长度
     * @return java.lang.Long
     * @author 阮雪峰
     * @date 2018/9/5 9:29
     */
    @JvmStatic fun getRandomNumber(length: Int): Long {
        return java.lang.Long.valueOf(getRandomNumberStr(length))
    }

    /**
     * 获取随机数
     * @param length 长度
     * @return java.lang.String
     * @author 阮雪峰
     * @date 2018/9/5 9:27
     */
    @JvmStatic fun getRandomNumberStr(length: Int): String {
        return RandomStringUtils.random(length, NUM)
    }

    /**
     * 获取随机字符串
     * @param length 随机字符串长度
     * @return java.lang.String
     * @author 阮雪峰
     * @date 2018/9/5 9:26
     */

    @JvmStatic fun getRandomStr(length: Int): String {
        return RandomStringUtils.random(length, CHARS)
    }

}
