package com.am.server.api.permission.advice;

import cn.hutool.core.codec.Base64;
import com.am.server.api.permission.config.PermissionConfig;
import com.am.server.api.permission.service.PermissionService;
import com.am.server.api.user.pojo.ao.LoginAo;
import com.am.server.api.user.pojo.vo.LoginUserInfoVo;
import com.am.server.api.user.pojo.vo.UserInfoVo;
import com.am.server.common.base.enumerate.Gender;
import com.am.server.common.base.service.CommonService;
import com.am.server.common.util.JwtUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 登录切面
 *
 * @author 阮雪峰
 * @date 2020年4月25日19:42:56
 */
@Aspect
@Component
public class LoginAdvice {

    private final CommonService commonService;

    private final PermissionConfig permissionConfig;

    private final PermissionService permissionService;

    public LoginAdvice(CommonService commonService, PermissionConfig permissionConfig, PermissionService permissionService) {
        this.commonService = commonService;
        this.permissionConfig = permissionConfig;
        this.permissionService = permissionService;
    }

    /**
     * 登录切面，判断是否为超级管理员登录
     *
     * @param pjp pjp
     * @return Object
     * @throws Throwable Throwable
     */
    @Around("execution(* *..AdminUserService.login(..))")
    public Object login(ProceedingJoinPoint pjp) throws Throwable {
        LoginAo loginAo = (LoginAo) pjp.getArgs()[0];
        String inputPassword = Base64.decodeStr(loginAo.getPassword());
        if (permissionConfig.getEnableSuperUser()
                && loginAo.getUsername().equals(permissionConfig.getUsername())
                && inputPassword.equals(permissionConfig.getPassword())) {
            return new LoginUserInfoVo(JwtUtils.sign(permissionConfig.getId().toString()));
        } else {
            return pjp.proceed();
        }
    }

    /**
     * 用户信息切面，如果是超级管理员默认赋值
     *
     * @param pjp pjp
     * @return Object
     * @throws Throwable Throwable
     */
    @Around("execution(* *..AdminUserService.info(..))")
    public Object info(ProceedingJoinPoint pjp) throws Throwable {

        if (permissionConfig.getEnableSuperUser() && commonService.isSupperUser()) {
            return new UserInfoVo()
                    .setId(commonService.getLoginUserId())
                    .setPermissions(permissionService.findAllPermissionRemarkList())
                    .setAvatar("https://ruanxuefeng.gitee.io/source/am/admin-avatar.gif")
                    .setEmail("admin@am.com")
                    .setGender(Gender.男)
                    .setName("超级管理员")
                    .setRoles(Arrays.asList("这管理员", "那管理员"));
        } else {
            return pjp.proceed();
        }
    }
}
