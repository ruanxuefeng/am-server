package com.am.server.api.user.controller;

import com.am.server.api.log.aspect.annotation.WriteLog;
import com.am.server.api.permission.annotation.Menu;
import com.am.server.api.permission.annotation.Permission;
import com.am.server.api.user.pojo.ao.*;
import com.am.server.api.user.pojo.vo.AdminUserListVo;
import com.am.server.api.user.service.AdminUserService;
import com.am.server.common.base.controller.BaseController;
import com.am.server.common.base.pojo.vo.Exist;
import com.am.server.common.base.pojo.vo.MessageVO;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.base.validator.Id;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.JwtUtils;
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
 * 用户管理
 *
 * @author 阮雪峰
 * @date 2018/7/25 13:26
 */
@Api(tags = "用户管理")
@Permission(value = "system-user", name = "用户管理", sort = 1, menus = {@Menu(value = "system", name = "系统管理", sort = 1)})
@WriteLog("用户管理")
@RestController
@RequestMapping(Constant.ADMIN_ROOT + "/user")
public class AdminUserController extends BaseController {

    private static final String EMAIL_EXIST = "user.email.exist";
    private static final String USERNAME_EXIST = "user.username.exist";
    private static final String AVATAR_BLANK = "user.avatar.blank";
    private static final String PASSWORD_RESET_SUCCESS = "user.password.reset.success";
    private static final String NOT_ALLOW_DELETE_YOURSELF = "user.not.allow.deleteYourself";

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    /**
     * 列表
     *
     * @param list list
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/25 15:56
     */
    @Permission(value = "system-user-list", name = "列表")
    @ApiOperation(value = "列表")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("/list")
    public ResponseEntity<PageVO<AdminUserListVo>> list(AdminUserListAo list) {
        return ResponseEntity.ok(adminUserService.list(list));
    }

    /**
     * 新增
     *
     * @param user 用户信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/25 15:56
     */
    @Permission(value = "system-user-add", name = "新增")
    @ApiOperation(value = "新增")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("新增")
    @PostMapping("/save")
    public ResponseEntity<MessageVO> save(@Validated SaveAdminUserAo user) {
        //检验邮箱是否存在
        if (adminUserService.isEmailExist(user.getEmail())) {
            return new ResponseEntity<>(message.get(EMAIL_EXIST), HttpStatus.BAD_REQUEST);
        }

        if (adminUserService.isUsernameExist(user.getUsername())) {
            return new ResponseEntity<>(message.get(USERNAME_EXIST), HttpStatus.BAD_REQUEST);
        }

        if (user.getImg() == null || user.getEmail().isEmpty()) {
            return new ResponseEntity<>(message.get(AVATAR_BLANK), HttpStatus.BAD_REQUEST);
        }

        adminUserService.save(user);

        return new ResponseEntity<>(message.get(SAVE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 修改
     *
     * @param user 用户信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/25 15:56
     */
    @Permission(value = "system-user-update", name = "修改")
    @ApiOperation(value = "修改")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("修改")
    @PostMapping("/update")
    public ResponseEntity<MessageVO> update(@Validated UpdateAdminUserAo user) {
        if (adminUserService.isEmailExistWithId(user.getEmail(), user.getId())) {
            return new ResponseEntity<>(message.get(EMAIL_EXIST), HttpStatus.BAD_REQUEST);
        }

        if (adminUserService.isUsernameExistWithId(user.getUsername(), user.getId())) {
            return new ResponseEntity<>(message.get(USERNAME_EXIST), HttpStatus.BAD_REQUEST);
        }

        adminUserService.update(user);

        return new ResponseEntity<>(message.get(SAVE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 删除
     *
     * @param id    用户主键
     * @param token 登录凭证
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/25 16:24
     */
    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "id", value = "用户id", example = "123456789", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)
    })
    @WriteLog("删除")
    @DeleteMapping("/delete")
    public ResponseEntity<MessageVO> delete(@RequestHeader(Constant.TOKEN) String token, Long id) {
        if (id == null) {
            return new ResponseEntity<>(message.get(COMMON_DELETE_PRIMARY_KEY_NULL), HttpStatus.BAD_REQUEST);
        }
        if (id.equals(Long.valueOf(JwtUtils.getSubject(token)))) {
            return new ResponseEntity<>(message.get(NOT_ALLOW_DELETE_YOURSELF), HttpStatus.BAD_REQUEST);
        }

        adminUserService.delete(id);

        return new ResponseEntity<>(message.get(DELETE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 重置密码
     *
     * @param id 用户主键
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/25 16:55
     */
    @ApiOperation(value = "重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "id", value = "用户id", example = "123456789", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)
    })
    @WriteLog("重置密码")
    @PostMapping("/reset/password")
    public ResponseEntity<MessageVO> resetPassword(Long id) {
        if (id == null) {
            return new ResponseEntity<>(message.get(COMMON_OPERATE_PRIMARY_KEY_NULL), HttpStatus.BAD_REQUEST);
        }
        adminUserService.resetPassword(id);
        return new ResponseEntity<>(message.get(PASSWORD_RESET_SUCCESS), HttpStatus.OK);
    }

    /**
     * 修改角色
     *
     * @param updateRole 用户信息
     * @author 阮雪峰
     * @date 2018/7/27 15:42
     */
    @Permission(check = false)
    @ApiOperation(value = "修改角色")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @WriteLog("修改角色")
    @PostMapping("/updateRole")
    public ResponseEntity<MessageVO> updateRole(@Validated(Id.class) @RequestBody UpdateRoleAo updateRole) {
        adminUserService.updateRole(updateRole.getId(), updateRole.getRoleIdList());
        return ResponseEntity.ok(message.get(UPDATE_SUCCESS));
    }

    /**
     * 邮箱是否被使用
     *
     * @param emailExist 用户信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/30 12:49
     */
    @ApiOperation(value = "邮件是否已经被注册")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("/isEmailExist")
    public ResponseEntity<Exist> isEmailExist(EmailExistAo emailExist) {
        Exist exist = Optional.of(emailExist)
                .map(EmailExistAo::getId)
                .map(id -> new Exist(adminUserService.isEmailExistWithId(emailExist.getEmail(), id)))
                .orElseGet(() -> new Exist(adminUserService.isEmailExist(emailExist.getEmail())));
        return new ResponseEntity<>(exist, HttpStatus.OK);
    }

    /**
     * 用户名是否被使用
     *
     * @param usernameExist usernameExist
     * @return org.springframework.http.ResponseEntity
     * @date 2019/4/10 9:23
     * @author 阮雪峰
     */
    @ApiOperation(value = "用户名是否被使用")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)})
    @GetMapping("/isUsernameExist")
    public ResponseEntity<Exist> isUsernameExist(UsernameExistAo usernameExist) {
        Exist exist = Optional.of(usernameExist)
                .map(UsernameExistAo::getId)
                .map(id -> new Exist(adminUserService.isUsernameExistWithId(usernameExist.getUsername(), id)))
                .orElseGet(() -> new Exist(adminUserService.isUsernameExist(usernameExist.getUsername())));

        return new ResponseEntity<>(exist, HttpStatus.OK);
    }

    /**
     * 角色id list
     *
     * @param id id
     * @return org.springframework.http.ResponseEntity
     * @date 2019/4/10 9:24
     * @author 阮雪峰
     */
    @Permission(check = false)
    @ApiOperation(value = "查询用户拥有角色的id")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Long", name = "id", value = "用户id", example = "123456789", required = true),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = Constant.TOKEN, value = "登录凭证", required = true)
    })
    @GetMapping("roleIdList")
    public ResponseEntity<List<Long>> roleIdList(Long id) {
        return ResponseEntity.ok(adminUserService.findRoleIdList(id));
    }
}
