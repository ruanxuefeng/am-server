package com.am.server.common.constant;

/**
 *  常量
 * @author 阮雪峰
 * @date 2018/9/10 12:56
 */
public class Constant {
    /**
     * 初始密码
     */
    public static final String INITIAL_PASSWORD = "123456";

    /**
     * 根目录
     */
    public static final String ROOT = "/api";
    public static final String ADMIN_ROOT = ROOT + "/admin";
    public static final String WEB_ROOT = ROOT + "/web";
    public static final String APP_ROOT = ROOT + "/app";

    public static final String TOKEN = "AM-TOKEN";

    public static final String CREATOR_NAME = "creatorName";

    private Constant() {
    }
}
