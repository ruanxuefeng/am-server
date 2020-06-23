package com.am.server.api.task.controller;

import com.am.server.api.log.aspect.annotation.WriteLog;
import com.am.server.api.permission.annotation.Menu;
import com.am.server.api.permission.annotation.Permission;
import com.am.server.api.task.pojo.ao.SaveScheduledTaskAo;
import com.am.server.api.task.pojo.ao.ScheduledTaskListAo;
import com.am.server.api.task.pojo.ao.UpdateScheduledTaskAo;
import com.am.server.api.task.pojo.vo.ScheduledTaskListListVo;
import com.am.server.api.task.service.ScheduledTaskService;
import com.am.server.common.base.controller.BaseController;
import com.am.server.common.base.pojo.vo.MessageVO;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.constant.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 定时任务
 *
 * @author 阮雪峰
 * @date 2020年4月26日21:03:00
 */
@Api(tags =  ScheduledTaskController.MENU_NAME)
@Permission(value = "system-scheduled-task", name = ScheduledTaskController.MENU_NAME, sort = 3, menus = {@Menu(value = "system", name = "系统管理", sort = 1)})
@WriteLog(ScheduledTaskController.MENU_NAME)
@RequestMapping(Constant.ADMIN_ROOT + "/scheduled/task")
@RestController
public class ScheduledTaskController extends BaseController {

    static final String MENU_NAME = "定时任务管理";

    private final ScheduledTaskService scheduledTaskService;

    public ScheduledTaskController(ScheduledTaskService scheduledTaskService) {
        this.scheduledTaskService = scheduledTaskService;
    }

    @ApiOperation(value = "列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("/list")
    public ResponseEntity<PageVO<ScheduledTaskListListVo>> list(ScheduledTaskListAo scheduledTaskListAo) {
        return ResponseEntity.ok(scheduledTaskService.list(scheduledTaskListAo));
    }

    /**
     * 新增
     *
     * @param saveScheduledTask 定时任务信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/27 10:38
     */
    @ApiOperation(value = "新增")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("新增")
    @PostMapping("/save")
    public ResponseEntity<MessageVO> save(@Validated @RequestBody SaveScheduledTaskAo saveScheduledTask) {
        scheduledTaskService.save(saveScheduledTask);
        return new ResponseEntity<>(message.get(SAVE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 修改
     *
     * @param updateScheduledTaskAo 定时任务信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/27 10:41
     */
    @ApiOperation(value = "修改")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("修改")
    @PostMapping("/update")
    public ResponseEntity<MessageVO> update(@Validated @RequestBody UpdateScheduledTaskAo updateScheduledTaskAo) {
        scheduledTaskService.update(updateScheduledTaskAo);
        return new ResponseEntity<>(message.get(UPDATE_SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(value = "修改状态")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("修改状态")
    @PostMapping("/update/status")
    public ResponseEntity<MessageVO> updateStatus(Long id) {
        scheduledTaskService.updateStatus(id);
        return new ResponseEntity<>(message.get(UPDATE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param id 定时任务信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/27 10:43
     */
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "id", value = "定时任务id", example = "123456789", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)
    })
    @WriteLog("删除")
    @DeleteMapping("/delete")
    public ResponseEntity<MessageVO> delete(Long id) {
        return Optional.ofNullable(id)
                .map(i -> {
                    scheduledTaskService.delete(id);
                    return ResponseEntity.ok(message.get(DELETE_SUCCESS));
                })
                .orElse(new ResponseEntity<>(message.get(COMMON_DELETE_PRIMARY_KEY_NULL), HttpStatus.OK));
    }

    @ApiOperation(value = "直接触发")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "id", value = "定时任务id", example = "123456789", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)
    })
    @WriteLog("直接触发")
    @PutMapping("/trigger")
    public ResponseEntity<MessageVO> trigger(Long id) {
        return Optional.ofNullable(id)
                .map(i -> {
                    scheduledTaskService.trigger(id);
                    return ResponseEntity.ok(message.get(SUCCESS));
                })
                .orElse(new ResponseEntity<>(message.get(COMMON_DELETE_PRIMARY_KEY_NULL), HttpStatus.OK));
    }
}
