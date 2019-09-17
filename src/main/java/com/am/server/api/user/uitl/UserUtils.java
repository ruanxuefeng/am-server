package com.am.server.api.user.uitl;

import com.am.server.common.base.exception.NoTokenException;

import java.util.Optional;

/**
 * 用户相关工具类
 * @author 阮雪峰
 * @date 2019/1/18 12:26
 */
public class UserUtils {
    private UserUtils() {
    }

    /**
     * 用户是否登录
     * @param token token
     * @author 阮雪峰
     * @date 2019/1/18 12:30
     */
    public static void assertLogin(String token) {
        //没有token说明未登录
        Optional.ofNullable(token)
                .filter(t -> !t.isEmpty())
                .orElseThrow(NoTokenException::new);
    }
}
