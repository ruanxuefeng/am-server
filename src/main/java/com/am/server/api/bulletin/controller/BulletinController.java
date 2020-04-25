package com.am.server.api.bulletin.controller;

import com.am.server.api.bulletin.pojo.ao.BulletinListAo;
import com.am.server.api.bulletin.pojo.ao.SaveBulletinAo;
import com.am.server.api.bulletin.pojo.ao.UpdateBulletinAo;
import com.am.server.api.bulletin.pojo.vo.BulletinListVo;
import com.am.server.api.bulletin.service.BulletinService;
import com.am.server.api.log.aspect.annotation.WriteLog;
import com.am.server.api.permission.annotation.Permission;
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
 * 公告
 *
 * @author 阮雪峰
 * @date 2018/11/13 10:52
 */
@Api(tags = "公告管理")
@WriteLog("公告管理")
@Permission(value = "bulletin", name = "公告管理", sort = 30)
@RestController
@RequestMapping(Constant.ADMIN_ROOT + "/bulletin")
public class BulletinController extends BaseController {

    private static final String CHOOSE_BULLETIN = "bulletin.choose.publish";

    private final BulletinService bulletinService;

    public BulletinController(BulletinService bulletinService) {
        this.bulletinService = bulletinService;
    }


    /**
     * list
     *
     * @param bulletinListAo bulletinListAo
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/11/13 11:02
     */
    @ApiOperation(value = "列表查询")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("/list")
    public ResponseEntity<PageVO<BulletinListVo>> list(BulletinListAo bulletinListAo) {
        return ResponseEntity.ok(bulletinService.list(bulletinListAo));
    }

    /**
     * 新增
     *
     * @param saveBulletinAo saveBulletinAo
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/11/13 12:56
     */
    @ApiOperation(value = "新增")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("新增")
    @PostMapping("/save")
    public ResponseEntity<MessageVO> save(@Validated @RequestBody SaveBulletinAo saveBulletinAo) {
        bulletinService.save(saveBulletinAo);
        return ResponseEntity.ok(message.get(SAVE_SUCCESS));
    }


    /**
     * update
     *
     * @param updateBulletinAo updateBulletinAo
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/11/13 12:57
     */
    @ApiOperation(value = "修改")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("修改")
    @PostMapping("/update")
    public ResponseEntity<MessageVO> update(@Validated @RequestBody UpdateBulletinAo updateBulletinAo) {
        bulletinService.update(updateBulletinAo);
        return ResponseEntity.ok(message.get(UPDATE_SUCCESS));
    }

    /**
     * 发布
     *
     * @param id id
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/11/13 13:04
     */
    @ApiOperation(value = "发布")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("发布")
    @PostMapping("publish")
    public ResponseEntity<MessageVO> publish(Long id) {
        return Optional.ofNullable(id)
                .map(key -> {
                    bulletinService.publish(key);
                    return ResponseEntity.ok(message.get(SUCCESS));
                }).orElse(new ResponseEntity<>(message.get(CHOOSE_BULLETIN), HttpStatus.BAD_REQUEST));

    }

    /**
     * 删除
     *
     * @param id id
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/11/13 15:58
     */
    @ApiOperation(value = "删除")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("删除")
    @PostMapping("/delete")
    public ResponseEntity<MessageVO> delete(Long id) {
        return Optional.ofNullable(id)
                .map(i -> {
                    bulletinService.delete(id);
                    return ResponseEntity.ok(message.get(DELETE_SUCCESS));
                })
                .orElse(new ResponseEntity<>(message.get(COMMON_DELETE_PRIMARY_KEY_NULL), HttpStatus.OK));
    }
}
