package com.am.server.api.admin.role.controller;

import com.am.server.api.admin.log.aspect.annotation.WriteLog;
import com.am.server.api.admin.menu.service.MenuService;
import com.am.server.api.admin.role.entity.Role;
import com.am.server.api.admin.role.service.RoleService;
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
 *  角色管理
 * @author 阮雪峰
 * @date 2018/7/27 10:31
 */
@Permission("system-role")
@WriteLog("角色管理")
@RestController
@RequestMapping(Constant.ADMIN_ROOT + "/role")
public class RoleController extends BaseController {

    private final RoleService roleService;

    private final MenuService menuService;

    public RoleController(RoleService roleService, MenuService menuService) {
        this.roleService = roleService;
        this.menuService = menuService;
    }

    /**
     * 列表
     * @param page 分页
     * @param role 角色信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/27 10:33
     */
    @GetMapping("/list")
    public ResponseEntity list(PageVO<Role> page, Role role) {
        roleService.list(page, role);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * 新增
     * @param role 角色信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/27 10:38
     */
    @WriteLog("新增")
    @PostMapping("/save")
    public ResponseEntity save(@Validated(Save.class) @RequestBody Role role) {
        roleService.save(role);
        return new ResponseEntity<>(message.get(SAVE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 修改
     * @param role 角色信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/27 10:41
     */
    @WriteLog("修改")
    @PostMapping("/update")
    public ResponseEntity update(@Validated({Id.class, Update.class}) @RequestBody Role role) {
        roleService.update(role);
        return new ResponseEntity<>(message.get(UPDATE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 删除
     * @param role 角色信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/27 10:43
     */
    @WriteLog("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@Validated(Delete.class) @RequestBody Role role) {
        roleService.delete(role);
        return new ResponseEntity<>(message.get(DELETE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 树形结构所有的菜单
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/30 16:45
     */
    @GetMapping("/menu/list")
    public ResponseEntity menuList() {
        return ResponseEntity.ok(menuService.menuList());
    }

    /**
     * 角色拥有的权限
     * @param role role
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2019/2/14 8:46
     */
    @GetMapping("/menus")
    public ResponseEntity menus(@Validated({Id.class}) Role role) {
        return ResponseEntity.ok(roleService.getMenuList(role));
    }

    /**
     * 修改权限
     * @param role 角色信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/31 8:58
     */
    @WriteLog("分配权限")
    @PostMapping("/update/menuList")
    public ResponseEntity updateMenuList(@Validated(Id.class) @RequestBody Role role) {
        roleService.updateMenuList(role);
        return ResponseEntity.ok(message.get(UPDATE_SUCCESS));
    }

    /**
     * 查询所有
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2019/2/18 15:04
     */
    @GetMapping("/all")
    public ResponseEntity all() {
        return ResponseEntity.ok(roleService.findAll());
    }
}
