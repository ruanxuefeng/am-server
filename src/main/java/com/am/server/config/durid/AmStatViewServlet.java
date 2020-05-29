package com.am.server.config.durid;

import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.druid.support.http.StatViewServlet;
import com.am.server.api.user.uitl.UserUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 重写durid的认证方法
 *
 * @author 阮雪峰
 * @date 2020年5月29日20:30:46
 */
@Slf4j
public class AmStatViewServlet extends StatViewServlet {

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (UserUtils.logged(request)) {
                super.service(request, response);
            } else {
                log.error("未登录请求SQL监控页面。IP：{}，时间：{}",
                        ServletUtil.getClientIP(request), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        } catch (ExpiredJwtException e) {
            log.error("非法请求SQL监控页面。IP：{}，时间：{}",
                    ServletUtil.getClientIP(request), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }
}
