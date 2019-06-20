package com.am.server.api.admin.user.controller;

import com.am.server.api.admin.log.aspect.annotation.WriteLog;
import com.am.server.api.admin.user.interceptor.annotation.Permission;
import com.am.server.api.admin.user.pojo.AdminUser;
import com.am.server.api.admin.user.pojo.param.SaveAdminUserAO;
import com.am.server.api.admin.user.pojo.param.ListQuery;
import com.am.server.api.admin.user.pojo.param.UpdateAdminUserAO;
import com.am.server.api.admin.user.service.AdminUserService;
import com.am.server.common.base.controller.BaseController;
import com.am.server.common.base.validator.Delete;
import com.am.server.common.base.validator.Id;
import com.am.server.common.base.validator.Save;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户管理
 *
 * @author 阮雪峰
 * @date 2018/7/25 13:26
 */
@Api(tags = "用户管理")
@Permission("system-user")
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
     * @param listQuery listQuery
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/25 15:56
     */
    @ApiOperation(value = "列表查询")
    @GetMapping("/list")
    public ResponseEntity list(ListQuery listQuery) {
        return new ResponseEntity<>(adminUserService.list(listQuery), HttpStatus.OK);
    }

    /**
     * 新增
     *
     * @param user 用户信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/25 15:56
     */
    @ApiOperation(value = "新增")
    @WriteLog("新增")
    @PostMapping("/save")
    public ResponseEntity save(@Validated(Save.class) SaveAdminUserAO user) {
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
    @WriteLog("修改")
    @PostMapping("/update")
    public ResponseEntity update(@Validated(Save.class) UpdateAdminUserAO user) {
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
     * @param user 用户信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/25 16:24
     */
    @WriteLog("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@RequestHeader(Constant.TOKEN) String token, @Validated(Delete.class) @RequestBody AdminUser user) {
        if (user.getId().equals(Long.valueOf(JwtUtils.getSubject(token)))) {
            return new ResponseEntity<>(message.get(NOT_ALLOW_DELETE_YOURSELF), HttpStatus.BAD_REQUEST);
        }

        adminUserService.delete(user);

        return new ResponseEntity<>(message.get(DELETE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 重置密码
     *
     * @param user 用户信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/25 16:55
     */
    @WriteLog("重置密码")
    @PostMapping("/reset/password")
    public ResponseEntity resetPassword(@Validated(Id.class) AdminUser user) {
        adminUserService.resetPassword(user);
        return new ResponseEntity<>(message.get(PASSWORD_RESET_SUCCESS), HttpStatus.OK);
    }

    /**
     * 修改角色
     *
     * @param user 用户信息
     * @author 阮雪峰
     * @date 2018/7/27 15:42
     */
    @WriteLog("修改角色")
    @PostMapping("/updateRole")
    public ResponseEntity updateRole(@Validated(Id.class) @RequestBody AdminUser user) {
        adminUserService.updateRole(user);
        return ResponseEntity.ok(message.get(UPDATE_SUCCESS));
    }

    /**
     * 邮箱是否被使用
     *
     * @param user 用户信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/30 12:49
     */
    @PostMapping("/isEmailExist")
    public ResponseEntity isEmailExist(AdminUser user) {
        Map<String, Boolean> map = new HashMap<>(1);
        boolean isExist = Optional.of(user)
                .map(AdminUser::getId)
                .map(id -> adminUserService.isEmailExistWithId(user.getUsername(), id))
                .orElseGet(() -> adminUserService.isEmailExist(user.getUsername()));
        map.put("isExist", isExist );
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * 用户名是否被使用
     *
     * @param user user
     * @return org.springframework.http.ResponseEntity
     * @date 2019/4/10 9:23
     * @author 阮雪峰
     */
    @PostMapping("/isUsernameExist")
    public ResponseEntity isUsernameExist(AdminUser user) {
        Map<String, Boolean> map = new HashMap<>(1);
        boolean isExist = Optional.of(user)
                .map(AdminUser::getId)
                .map(id -> adminUserService.isUsernameExistWithId(user.getUsername(), id))
                .orElseGet(() -> adminUserService.isUsernameExist(user.getUsername()));

        map.put("isExist", isExist);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * 角色id list
     *
     * @param user user
     * @return org.springframework.http.ResponseEntity
     * @date 2019/4/10 9:24
     * @author 阮雪峰
     */
    @GetMapping("roleIdList")
    public ResponseEntity roleIdList(AdminUser user) {
        return ResponseEntity.ok(adminUserService.findRoleIdList(user));
    }
}
