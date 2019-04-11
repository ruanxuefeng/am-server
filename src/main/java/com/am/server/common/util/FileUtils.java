package com.am.server.common.util;

/**
 * 文件工具类
 * @author 阮雪峰
 * @date 2019/2/18 14:27
 */
public class FileUtils {

    private FileUtils(){}

    /**
     * 获取文件名后缀
     * @param filename 文件名
     * @return java.lang.String
     * @author 阮雪峰
     * @date 2019/2/18 14:28
     */
    public static String getFileSuffix(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }
}
