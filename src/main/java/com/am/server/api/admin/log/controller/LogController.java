package com.am.server.api.admin.log.controller;

import com.am.server.api.admin.log.entity.Log;
import com.am.server.api.admin.log.service.LogService;
import com.am.server.common.base.page.Page;
import com.am.server.common.constant.Constant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  日志
 * @author 阮雪峰
 * @date 2018/8/1 13:15
 */
@RestController
@RequestMapping(Constant.ADMIN_ROOT+"/log")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    /**
     * 分页
     * @param page 分页
     * @param log 条件
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/8/22 9:44
     */
    @GetMapping("list")
    public ResponseEntity list(Page<Log> page, Log log) {
        logService.list(page, log);
        return ResponseEntity.ok(page);
    }
}
