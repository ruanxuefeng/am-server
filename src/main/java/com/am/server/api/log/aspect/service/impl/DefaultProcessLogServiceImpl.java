package com.am.server.api.log.aspect.service.impl;

import com.am.server.api.log.aspect.annotation.WriteLog;
import com.am.server.api.log.aspect.service.ProcessLogService;
import com.am.server.api.log.pojo.ao.SaveLogAO;
import com.am.server.api.log.service.LogService;
import com.am.server.api.user.exception.UserNotExistException;
import com.am.server.api.user.pojo.po.AdminUserDO;
import com.am.server.api.user.service.AdminUserService;
import com.am.server.common.base.service.CommonService;
import org.springframework.stereotype.Service;

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

    private final AdminUserService adminUserService;

    private final LogService logService;

    private final CommonService commonService;

    public DefaultProcessLogServiceImpl(AdminUserService adminUserService, LogService logService, CommonService commonService) {
        this.adminUserService = adminUserService;
        this.logService = logService;
        this.commonService = commonService;
    }

    @Override
    public void process(Class<?> targetClass, WriteLog targetClassWriteLog, Method targetMethod, WriteLog targetMethodWriteLog) {
        String name = Optional.ofNullable(adminUserService.findById(commonService.getLoginUserId()))
                .map(AdminUserDO::getName)
                .orElseThrow(UserNotExistException::new);

        SaveLogAO log = new SaveLogAO()
                .setMenu(Optional.ofNullable(targetClassWriteLog).map(WriteLog::value).orElse(""))
                .setOperate(Optional.ofNullable(targetMethodWriteLog).map(WriteLog::value).orElse(""))
                .setName(name)
                .setIp(commonService.getRequestIp());
        logService.save(log);
    }
}
