package com.am.server.common.util

object FileUtils{
    /**
     * 获取文件名后缀
     * @param filename 文件名
     * @return java.lang.String
     * @author 阮雪峰
     * @date 2019/2/18 14:28
     */
    @JvmStatic
    fun getFileSuffix(filename: String): String {
        return filename.substring(filename.lastIndexOf("."))
    }
}
