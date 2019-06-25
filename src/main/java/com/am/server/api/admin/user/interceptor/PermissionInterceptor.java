package com.am.server.api.admin.user.interceptor;

import com.am.server.api.admin.user.exception.IllegalRequestException;
import com.am.server.api.admin.user.service.UserPermissionCacheService;
import com.am.server.api.admin.user.interceptor.annotation.Permission;
import com.am.server.api.admin.user.exception.NoPermissionAccessException;
import com.am.server.common.base.exception.TokenExpiredException;
import com.am.server.api.admin.user.uitl.UserUtils;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

/**
 * 权限拦截器
 *
 * @author 阮雪峰
 * @date 2018/8/2 12:43
 */
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserPermissionCacheService userPermissionCacheService;

    private static final String OPTION = "OPTIONS";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //如果是预处理请求直接过
        String method = request.getMethod();
        if (OPTION.equalsIgnoreCase(method)) {
            return true;
        }

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            String url = request.getRequestURI();

            Permission methodPermission = handlerMethod.getMethodAnnotation(Permission.class);
            Permission classPermission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Permission.class);

            //判断在方法或者类上有没有加权限，如果都有以方法上为准
            Permission permission = Optional.ofNullable(methodPermission).orElse(classPermission);
            Optional.ofNullable(permission).ifPresent(p -> {
                String token = request.getHeader(Constant.TOKEN);
                //没有token说明未登录
                UserUtils.assertLogin(token);

                //没有获取到uid说明token过期或者不是token
                String uid;
                try {
                    uid = JwtUtils.getSubject(token);
                } catch (Exception e) {
                    throw new TokenExpiredException();
                }
                //判断是否拥有类访问权限
                if (permission.check() && !userPermissionCacheService.hasPermission(Long.valueOf(uid), permission.value())) {
                    throw new NoPermissionAccessException();
                }
            });
            return true;
        } else {
            throw new IllegalRequestException();
        }
    }
}
