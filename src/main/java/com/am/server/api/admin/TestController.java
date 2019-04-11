package com.am.server.api.admin;

import com.am.server.api.admin.log.entity.Log;
import com.am.server.api.admin.log.service.LogService;
import com.am.server.common.util.IpUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author 阮雪峰
 * @date 2018/11/27 13:18
 */
@Controller
public class TestController {
    @Resource(name = "logService")
    private LogService logService;

    @PostMapping("/confg.php")
    public ResponseEntity index(HttpServletRequest request) {
        Log log = new Log();
        log.setOperate("异常");
        log.setIp(IpUtils.getIpAddress(request));
        logService.save(log);
        return ResponseEntity.ok("你想干啥？");
    }
}
