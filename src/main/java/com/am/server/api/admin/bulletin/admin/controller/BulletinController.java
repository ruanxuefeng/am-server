package com.am.server.api.admin.bulletin.admin.controller;

import com.am.server.api.admin.bulletin.admin.entity.Bulletin;
import com.am.server.api.admin.bulletin.admin.service.BulletinService;
import com.am.server.api.admin.log.aspect.annotation.WriteLog;
import com.am.server.api.admin.user.interceptor.annotation.Permission;
import com.am.server.common.base.controller.BaseController;
import com.am.server.common.base.page.Page;
import com.am.server.common.base.validator.Delete;
import com.am.server.common.base.validator.Id;
import com.am.server.common.base.validator.Save;
import com.am.server.common.constant.Constant;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 公告
 * @author 阮雪峰
 * @date 2018/11/13 10:52
 */
@WriteLog("公告管理")
@Permission("bulletin")
@RestController("adminBulletinController")
@RequestMapping(Constant.ADMIN_ROOT + "/bulletin")
public class BulletinController extends BaseController {

    private final BulletinService bulletinService;

    public BulletinController(BulletinService bulletinService) {
        this.bulletinService = bulletinService;
    }


    /**
     * list
     * @param page page
     * @param bulletin bulletin
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/11/13 11:02
     */
    @GetMapping("/list")
    public ResponseEntity list(Page<Bulletin> page, Bulletin bulletin) {
        bulletinService.list(page, bulletin);
        return ResponseEntity.ok(page);
    }

    /**
     * 新增
     * @param bulletin bulletin
     * @param token token
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/11/13 12:56
     */
    @WriteLog("新增")
    @PostMapping("/save")
    public ResponseEntity save(@Validated(Save.class) @RequestBody Bulletin bulletin) {
        bulletinService.save(bulletin);
        return ResponseEntity.ok(message.get(SAVE_SUCCESS));
    }


    /**
     * update
     * @param bulletin bulletin
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/11/13 12:57
     */
    @WriteLog("修改")
    @PostMapping("/update")
    public ResponseEntity update(@RequestBody Bulletin bulletin) {
        bulletinService.update(bulletin);
        return ResponseEntity.ok(message.get(UPDATE_SUCCESS));
    }

    /**
     * 发布
     * @param bulletin bulletin
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/11/13 13:04
     */
    @WriteLog("发布")
    @PostMapping("publish")
    public ResponseEntity publish(@Validated(Id.class) @RequestBody Bulletin bulletin) {
        bulletinService.publish(bulletin);
        return ResponseEntity.ok(message.get(SUCCESS));
    }

    /**
     * 删除
     * @param bulletin bulletin
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/11/13 15:58
     */
    @WriteLog("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@Validated(Delete.class) @RequestBody Bulletin bulletin) {
        bulletinService.delete(bulletin);
        return ResponseEntity.ok(message.get(DELETE_SUCCESS));
    }

    /**
     * 查询首页展示的已发布的公告
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/11/13 14:31
     */
    @GetMapping("/dashboard")
    public ResponseEntity dashboard() {
        return ResponseEntity.ok(bulletinService.findPublishedAndUnexpiredList());
    }
}
