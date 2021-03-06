package com.am.server.api.permission.interceptor;

import com.am.server.api.permission.annotation.Permission;
import com.am.server.api.permission.config.PermissionConfig;
import com.am.server.api.user.exception.IllegalRequestException;
import com.am.server.api.user.exception.NoPermissionAccessException;
import com.am.server.api.user.service.UserPermissionCacheService;
import com.am.server.api.user.uitl.UserUtils;
import com.am.server.common.base.exception.NoTokenException;
import com.am.server.common.base.service.CommonService;
import com.am.server.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

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
    private CommonService commonService;

    @Autowired
    private PermissionConfig permissionConfig;

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

        if (!UserUtils.logged(request)) {
            throw new NoTokenException();
        }

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            Permission methodPermission = handlerMethod.getMethodAnnotation(Permission.class);
            Permission classPermission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Permission.class);

            //判断在方法或者类上有没有加权限，如果都有以方法上为准
            Permission permission = Optional.ofNullable(methodPermission).orElse(classPermission);

            Optional.ofNullable(permission).map(p -> {
                //如果允许超级管理员登录并且当前登录用户是超级管理员
                if (permissionConfig.getEnableSuperUser() && commonService.isSupperUser()) {
                    return true;
                }
                boolean hasPermission = userPermissionCacheService.hasPermission(commonService.getLoginUserId(), permission.value());
                if (permission.check() && !hasPermission) {
                    //没有获取到uid说明token过期或者不是token
                    //判断是否拥有类访问权限
                    throw new NoPermissionAccessException();
                } else {
                    return true;
                }
            });
            return true;
        } else if (handler instanceof ResourceHttpRequestHandler) {
            if (UserUtils.logged(request)) {
                return true;
            }else{
                throw new NoTokenException();
            }
        } else {
            throw new IllegalRequestException();
        }
    }

}
