package com.am.server.api.admin.menu.controller;

import com.am.server.api.admin.log.aspect.annotation.WriteLog;
import com.am.server.api.admin.menu.pojo.ao.MenuListAO;
import com.am.server.api.admin.menu.pojo.ao.SaveMenuAO;
import com.am.server.api.admin.menu.pojo.ao.UpdateMenuAO;
import com.am.server.api.admin.menu.pojo.vo.MenuListVO;
import com.am.server.api.admin.menu.pojo.vo.TreeMenuVO;
import com.am.server.api.admin.menu.service.MenuService;
import com.am.server.api.admin.role.pojo.vo.SelectRoleVO;
import com.am.server.api.admin.user.interceptor.annotation.Permission;
import com.am.server.common.base.controller.BaseController;
import com.am.server.common.base.pojo.vo.MessageVO;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.base.validator.Delete;
import com.am.server.common.constant.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理
 *
 * @author 阮雪峰
 * @date 2018/7/30 14:57
 */
@Api(tags = "菜单管理")
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
     * @param menuListAo menuListAo
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/30 15:00
     */
    @ApiOperation(value = "列表查询")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("/list")
    public ResponseEntity<PageVO<MenuListVO>> list(MenuListAO menuListAo) {
        return new ResponseEntity<>(menuService.list(menuListAo), HttpStatus.OK);
    }

    /**
     * 新增
     *
     * @param menu 菜单信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/30 15:02
     */
    @ApiOperation(value = "新增")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("新增")
    @PostMapping("/save")
    public ResponseEntity<MessageVO> save(@Validated @RequestBody SaveMenuAO menu) {
        menuService.save(menu);
        return ResponseEntity.ok(message.get(SAVE_SUCCESS));
    }

    /**
     * 修改
     *
     * @param menu 菜单信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/30 15:13
     */
    @ApiOperation(value = "修改")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("修改")
    @PostMapping("/update")
    public ResponseEntity<MessageVO> update(@Validated @RequestBody UpdateMenuAO menu) {
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
    @ApiOperation(value = "删除")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("删除")
    @DeleteMapping("/delete")
    public ResponseEntity<MessageVO> delete(@Validated(Delete.class) Long id) {
        menuService.delete(id);
        return ResponseEntity.ok(message.get(DELETE_SUCCESS));
    }

    /**
     * 查询父级
     *
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/30 16:07
     */
    @ApiOperation(value = "查询父级")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("/parent/list")
    public ResponseEntity<List<SelectRoleVO>> parentList() {
        return ResponseEntity.ok(menuService.parentList());
    }

    /**
     * 树形结构所有的菜单
     *
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/30 16:45
     */
    @ApiOperation(value = "所有菜单")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("/all/list")
    public ResponseEntity<List<TreeMenuVO>> allMenuList() {
        return ResponseEntity.ok(menuService.allMenuList());
    }
}
