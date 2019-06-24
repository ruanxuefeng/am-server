package com.am.server.api.admin.menu.controller;

import com.am.server.api.admin.log.aspect.annotation.WriteLog;
import com.am.server.api.admin.menu.entity.Menu;
import com.am.server.api.admin.menu.service.MenuService;
import com.am.server.api.admin.user.interceptor.annotation.Permission;
import com.am.server.common.base.controller.BaseController;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.base.validator.Delete;
import com.am.server.common.base.validator.Id;
import com.am.server.common.base.validator.Save;
import com.am.server.common.base.validator.Update;
import com.am.server.common.constant.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单管理
 *
 * @author 阮雪峰
 * @date 2018/7/30 14:57
 */
@Permission("system-menu")
@WriteLog("菜单管理")
@RestController
@RequestMapping(Constant.ADMIN_ROOT + "/menu")
public class MenuController extends BaseController {


    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 列表
     *
     * @param page 分页
     * @param menu 菜单信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/30 15:00
     */
    @GetMapping("/list")
    public ResponseEntity list(PageVO<Menu> page, Menu menu) {
        menuService.list(page, menu);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * 新增
     *
     * @param menu 菜单信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/30 15:02
     */
    @WriteLog("新增")
    @PostMapping("/save")
    public ResponseEntity save(@Validated(Save.class) @RequestBody Menu menu) {
        menuService.save(menu);
        return ResponseEntity.ok(message.get(SAVE_SUCCESS));
    }

    /**
     * 详情
     *
     * @param menu 菜单信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/30 15:09
     */
    @GetMapping("/detail")
    public ResponseEntity detail(@Validated(Id.class) Menu menu) {
        return ResponseEntity.ok(menuService.detail(menu));
    }

    /**
     * 修改
     *
     * @param menu 菜单信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/30 15:13
     */
    @WriteLog("修改")
    @PostMapping("/update")
    public ResponseEntity update(@Validated({Id.class, Update.class}) @RequestBody Menu menu) {
        menuService.update(menu);
        return ResponseEntity.ok(message.get(UPDATE_SUCCESS));
    }

    /**
     * 删除
     *
     * @param menu 菜单信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/30 15:16
     */
    @WriteLog("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@Validated(Delete.class) @RequestBody Menu menu) {
        menuService.delete(menu);
        return ResponseEntity.ok(message.get(DELETE_SUCCESS));
    }

    /**
     * 查询父级
     *
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/30 16:07
     */
    @GetMapping("/parent/list")
    public ResponseEntity parentList() {
        return ResponseEntity.ok(menuService.parentList());
    }
}
