package com.am.server.api.user.uitl;

import cn.hutool.extra.servlet.ServletUtil;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.JwtUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 用户相关工具类
 *
 * @author 阮雪峰
 * @date 2019/1/18 12:26
 */
public class UserUtils {
    private UserUtils() {
    }

    /**
     * 用户是否登录
     *
     * @param request request
     * @author 阮雪峰
     * @date 2019/1/18 12:30
     */
    public static boolean logged(HttpServletRequest request) {
        //请求头和Cookie中都没有token说明未登录
        boolean hadHeaderToken = Optional.ofNullable(request.getHeader(Constant.TOKEN))
                .map(JwtUtils::getSubject)
                .isPresent();
        boolean hedCookie = Optional.ofNullable(ServletUtil.getCookie(request, Constant.TOKEN))
                .map(Cookie::getValue)
                .map(JwtUtils::getSubject)
                .isPresent();
        return hadHeaderToken || hedCookie;
    }
}
