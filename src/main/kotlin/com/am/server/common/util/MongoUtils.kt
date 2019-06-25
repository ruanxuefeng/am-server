package com.am.server.common.util

import java.util.regex.Pattern

object MongoUtils {
    /**
     * 获取模糊查询正则
     * @ao keyWord keyWord
     * @return java.util.regex.Pattern
     * @author 阮雪峰
     * @date 2018/4/13 10:59
     */
    @JvmStatic
    fun getRegx(keyWord: String): Pattern {
        val regex = "^.*$keyWord.*$"
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
    }
}
