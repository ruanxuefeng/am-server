package com.am.server.api.log.aspect.service.impl;

import com.am.server.api.log.aspect.annotation.WriteLog;
import com.am.server.api.log.aspect.service.ProcessLogService;
import com.am.server.api.log.pojo.ao.SaveLogAo;
import com.am.server.api.log.service.LogService;
import com.am.server.api.user.dao.rdb.AdminUserDao;
import com.am.server.api.user.exception.UserNotExistException;
import com.am.server.api.user.pojo.po.AdminUserDo;
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

    private final AdminUserDao adminUserDAO;

    private final LogService logService;

    private final CommonService commonService;

    public DefaultProcessLogServiceImpl(AdminUserDao adminUserDAO, LogService logService, CommonService commonService) {
        this.adminUserDAO = adminUserDAO;
        this.logService = logService;
        this.commonService = commonService;
    }

    @Override
    public void process(Class<?> targetClass, WriteLog targetClassWriteLog, Method targetMethod, WriteLog targetMethodWriteLog) {
        String name = adminUserDAO.findById(commonService.getLoginUserId())
                .map(AdminUserDo::getUsername)
                .orElseThrow(UserNotExistException::new);

        SaveLogAo log = new SaveLogAo()
                .setMenu(Optional.ofNullable(targetClassWriteLog).map(WriteLog::value).orElse(""))
                .setOperate(Optional.ofNullable(targetMethodWriteLog).map(WriteLog::value).orElse(""))
                .setName(name)
                .setIp(commonService.getRequestIp());
        logService.save(log);
    }
}
