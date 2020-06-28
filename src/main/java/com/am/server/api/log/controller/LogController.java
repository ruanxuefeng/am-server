package com.am.server.api.log.controller;

import com.am.server.api.log.pojo.ao.LogListAo;
import com.am.server.api.log.pojo.vo.LogListVo;
import com.am.server.api.log.service.LogService;
import com.am.server.api.permission.annotation.Menu;
import com.am.server.api.permission.annotation.Permission;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.constant.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志
 *
 * @author 阮雪峰
 * @date 2018/8/1 13:15
 */
@Api(tags = LogController.MENU_NAME)
@Permission(value = "log-operate", name = LogController.MENU_NAME, sort = 0, menus = {@Menu(value = "log", name = "日志管理", sort = 20)})
@RestController
@RequestMapping(Constant.ADMIN_ROOT + "/log")
public class LogController {

    public static final String MENU_NAME = "用户操作日志";

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    /**
     * 分页
     *
     * @param logListAo logListAo
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/8/22 9:44
     */
    @ApiOperation("列表查询")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("list")
    public ResponseEntity<PageVO<LogListVo>> list(LogListAo logListAo) {
        return ResponseEntity.ok(logService.list(logListAo));
    }
}
