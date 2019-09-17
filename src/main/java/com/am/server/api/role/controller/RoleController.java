package com.am.server.api.role.controller;

import com.am.server.api.log.aspect.annotation.WriteLog;
import com.am.server.api.role.pojo.ao.RoleListAO;
import com.am.server.api.role.pojo.ao.SaveRoleAO;
import com.am.server.api.role.pojo.ao.UpdateRoleMenuAO;
import com.am.server.api.role.pojo.ao.UpdateRoleAO;
import com.am.server.api.role.pojo.vo.RoleListVo;
import com.am.server.api.role.pojo.vo.SelectRoleVO;
import com.am.server.api.role.service.RoleService;
import com.am.server.api.user.interceptor.annotation.Permission;
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
 *  角色管理
 * @author 阮雪峰
 * @date 2018/7/27 10:31
 */
@Api(tags = "角色管理")
@Permission("system-role")
@WriteLog("角色管理")
@RestController
@RequestMapping(Constant.ADMIN_ROOT + "/role")
public class RoleController extends BaseController {

    private final RoleService roleService;


    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 列表
     * @param roleListAo roleListAo
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/27 10:33
     */
    @ApiOperation(value = "列表查询")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("/list")
    public ResponseEntity<PageVO<RoleListVo>> list(RoleListAO roleListAo) {
        return ResponseEntity.ok(roleService.list(roleListAo));
    }

    /**
     * 新增
     * @param role 角色信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/27 10:38
     */
    @ApiOperation(value = "新增")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("新增")
    @PostMapping("/save")
    public ResponseEntity<MessageVO> save(@Validated @RequestBody SaveRoleAO role) {
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
    @ApiOperation(value = "修改")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("修改")
    @PostMapping("/update")
    public ResponseEntity<MessageVO> update(@Validated @RequestBody UpdateRoleAO roleAo) {
        roleService.update(roleAo);
        return new ResponseEntity<>(message.get(UPDATE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 删除
     * @param role 角色信息
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
     * 角色拥有的权限
     * @param role role
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2019/2/14 8:46
     */
    @ApiOperation(value = "角色拥有的权限的id")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("/menus")
    public ResponseEntity<List<Long>> menus(Long id) {
        return ResponseEntity.ok(roleService.getMenuList(id));
    }

    /**
     * 修改权限
     * @param role 角色信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/31 8:58
     */
    @ApiOperation(value = "分配权限")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("分配权限")
    @PostMapping("/update/menuList")
    public ResponseEntity updateMenuList(@RequestBody UpdateRoleMenuAO updateRoleMenuAo) {
        roleService.updateMenuList(updateRoleMenuAo);
        return ResponseEntity.ok(message.get(UPDATE_SUCCESS));
    }

    /**
     * 查询所有
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2019/2/18 15:04
     */
    @ApiOperation(value = "查询所有")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("/all")
    public ResponseEntity<List<SelectRoleVO>> all() {
        return ResponseEntity.ok(roleService.findAll());
    }
}
