package com.am.server.common.util;

/**
 *
 * @author 阮雪峰
 * @date 2018/7/11 14:53
 */
public class RegularUtils {

    /**
     * 整数或者小数
     */
    public static final String NUMBER = "^[0-9]+\\.{0,1}[0-9]{0,2}$";

    /**
     * 只能输入有两位小数的正实数
     */
    public static final String TOW_BIT_DECIMAL = "^[0-9]+(.[0-9]{2})?$";

    /**
     * 只能输入有1~3位小数的正实数
     */
    public static final String ONE_TO_THREE_DECIMAL = "^[0-9]+(.[0-9]{1,3})?$";

    /**
     * 只能输入非零的正整数
     */
    public static final String NOT_ZERO_POSITIVE_INTEGER = "^\\+?[1-9][0-9]*$";

    /**
     * 只能输入由数字、26个英文字母或者下划线组成的字符串
     */
    public static final String NORMAL_STRING = "^\\w+$";

    /**
     * 用户名。正确格式为：以字母开头，长度在6~18之间，只能包含字符、数字和下划线。
     */
    public static final String USER_NAME = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 密码校验，暂时与用户名一样
     */
    public static final String PASSWORD = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 验证是否含有^%&',;=?$\"
     */
    public static final String SPECIAL_CHARACTERS = "[^%&',;=?$\\x22]+";

    /**
     * 只能输入汉字
     */
    public static final String CHINESE = "^[\u4e00-\u9fa5]{0,}$";

    /**
     * 邮件
     */
    public static final String EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /**
     * 手机号码
     */
    public static final String MOBELPHONE = "^(1)\\d{10}$";

    /**
     * 图片
     */
    public static final String IMAGE = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$";

    /**
     * 视频
     */
    public static final String VIDEO = ".+(.mp4|.MP4|.avi|.AVI|.mov|.MOV|.rmvb|.RMVB|.wmv|.WMV)$";

    /**
     * 文本
     */
    public static final String TEXT = ".+(.pdf|.PDF|.txt|.TXT|.doc|.DOC)$";
    /**
     * 压缩包
     */
    public static final String ZIP = ".+(.zip|.ZIP|.rar|.RAR)$";

    private RegularUtils() {}

    /**
     * 字符串校验
     * @param str 校验字符串
     * @param reg 正则
     * @return boolean
     * @author 阮雪峰
     * @date 2018/9/10 12:57
     */
    public static boolean matches(String str, String reg) {
        if ((str == null)) {
            throw new RuntimeException("待验证字符串为空");
        }

        return str.matches(reg);
    }

}
