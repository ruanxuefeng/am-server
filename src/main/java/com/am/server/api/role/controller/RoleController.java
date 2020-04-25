package com.am.server.api.role.controller;

import com.am.server.api.log.aspect.annotation.WriteLog;
import com.am.server.api.permission.annotation.Menu;
import com.am.server.api.permission.annotation.Permission;
import com.am.server.api.permission.service.PermissionService;
import com.am.server.api.role.pojo.ao.*;
import com.am.server.api.role.pojo.vo.PermissionTreeVo;
import com.am.server.api.role.pojo.vo.RoleListVo;
import com.am.server.api.role.pojo.vo.SelectRoleVo;
import com.am.server.api.role.service.RoleService;
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

import java.util.List;
import java.util.Optional;

/**
 * 角色管理
 *
 * @author 阮雪峰
 * @date 2018/7/27 10:31
 */
@Api(tags = "角色管理")
@Permission(value = "system-role", name = "角色管理", sort = 2, menus = {@Menu(value = "system", name = "系统管理", sort = 1)})
@WriteLog("角色管理")
@RestController
@RequestMapping(Constant.ADMIN_ROOT + "/role")
public class RoleController extends BaseController {

    private final RoleService roleService;

    private final PermissionService permissionService;


    public RoleController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    /**
     * 列表
     *
     * @param roleListAo roleListAo
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/27 10:33
     */
    @ApiOperation(value = "列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("/list")
    public ResponseEntity<PageVO<RoleListVo>> list(RoleListAo roleListAo) {
        return ResponseEntity.ok(roleService.list(roleListAo));
    }

    /**
     * 新增
     *
     * @param role 角色信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/27 10:38
     */
    @ApiOperation(value = "新增")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("新增")
    @PostMapping("/save")
    public ResponseEntity<MessageVO> save(@Validated @RequestBody SaveRoleAo role) {
        roleService.save(role);
        return new ResponseEntity<>(message.get(SAVE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 修改
     *
     * @param roleAo 角色信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/27 10:41
     */
    @ApiOperation(value = "修改")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("修改")
    @PostMapping("/update")
    public ResponseEntity<MessageVO> update(@Validated @RequestBody UpdateRoleAo roleAo) {
        roleService.update(roleAo);
        return new ResponseEntity<>(message.get(UPDATE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param id 角色信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/27 10:43
     */
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "id", value = "用户id", example = "123456789", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)
    })
    @WriteLog("删除")
    @DeleteMapping("/delete")
    public ResponseEntity<MessageVO> delete(Long id) {
        return Optional.ofNullable(id)
                .map(i -> {
                    roleService.delete(id);
                    return ResponseEntity.ok(message.get(DELETE_SUCCESS));
                })
                .orElse(new ResponseEntity<>(message.get(COMMON_DELETE_PRIMARY_KEY_NULL), HttpStatus.OK));
    }

    /**
     * 获取权限树
     * @return ResponseEntity<List<PermissionTreeVO>>
     */
    @ApiOperation(value = "获取权限树")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("/permission/tree")
    public ResponseEntity<List<PermissionTreeVo>> permissionTree() {
        return ResponseEntity.ok(permissionService.findAll());
    }

    /**
     * 角色拥有的权限
     *
     * @param id role
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2019/2/14 8:46
     */
    @ApiOperation(value = "获取角色拥有的权限")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("/permissions")
    public ResponseEntity<List<String>> permissions(Long id) {
        return ResponseEntity.ok(roleService.findPermissions(id));
    }

    /**
     * 修改权限
     *
     * @param updateRolePermissionAo 角色信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/31 8:58
     */
    @ApiOperation(value = "修改角色权限")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @PostMapping("/update/permissions")
    public ResponseEntity<MessageVO> updatePermissions(@RequestBody UpdateRolePermissionAo updateRolePermissionAo) {
        roleService.updatePermissions(updateRolePermissionAo);
        return ResponseEntity.ok(message.get(UPDATE_SUCCESS));
    }
    /**
     * 查询所有
     *
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2019/2/18 15:04
     */
    @ApiOperation(value = "查询所有")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("/all")
    public ResponseEntity<List<SelectRoleVo>> all() {
        return ResponseEntity.ok(roleService.findAll());
    }
}
