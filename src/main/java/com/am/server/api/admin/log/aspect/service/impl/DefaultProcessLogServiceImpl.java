package com.am.server.api.admin.log.aspect.service.impl;

import com.am.server.api.admin.log.aspect.annotation.WriteLog;
import com.am.server.api.admin.log.aspect.service.ProcessLogService;
import com.am.server.api.admin.log.entity.Log;
import com.am.server.api.admin.log.service.LogService;
import com.am.server.api.admin.user.exception.TokenExpiredException;
import com.am.server.api.admin.user.exception.UserNotExistException;
import com.am.server.api.admin.user.pojo.po.AdminUserPO;
import com.am.server.api.admin.user.service.AdminUserService;
import com.am.server.api.admin.user.uitl.UserUtils;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.IpUtils;
import com.am.server.common.util.JwtUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 默认处理日志实现
 *
 * @author 阮雪峰
 * @date 2019/3/27 14:35
 */
@Service
public class DefaultProcessLogServiceImpl implements ProcessLogService {

    @Resource(name = "adminUserService")
    private AdminUserService adminUserService;

    @Resource(name = "logService")
    private LogService logService;


    @Override
    public void process(Class<?> targetClass, WriteLog targetClassWriteLog, Method targetMethod, WriteLog targetMethodWriteLog) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        Optional.ofNullable(servletRequestAttributes)
                .ifPresent(attributes -> {
                    HttpServletRequest request = attributes.getRequest();

                    String token = request.getHeader(Constant.TOKEN);

                    UserUtils.assertLogin(token);

                    //没有获取到uid说明token过期或者不是token
                    String uid;
                    try {
                        uid = JwtUtils.getSubject(token);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new TokenExpiredException();
                    }
                    String name = Optional.ofNullable(adminUserService.findById(Long.valueOf(uid)))
                            .map(AdminUserPO::getName)
                            .orElseThrow(UserNotExistException::new);

                    Log log = new Log();
                    log.setMenu(Optional.ofNullable(targetClassWriteLog).map(WriteLog::value).orElse(""));
                    log.setOperate(Optional.ofNullable(targetMethodWriteLog).map(WriteLog::value).orElse(""));
                    log.setName(name);
                    log.setIp(IpUtils.getIpAddress(request));

                    logService.save(log);

                });
    }
}
