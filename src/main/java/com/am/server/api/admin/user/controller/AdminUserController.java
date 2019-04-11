package com.am.server.api.admin.user.controller;

import com.am.server.api.admin.log.aspect.annotation.WriteLog;
import com.am.server.api.admin.user.interceptor.annotation.Permission;
import com.am.server.api.admin.user.entity.AdminUser;
import com.am.server.api.admin.user.service.AdminUserService;
import com.am.server.common.base.controller.BaseController;
import com.am.server.common.base.page.Page;
import com.am.server.common.base.validator.Delete;
import com.am.server.common.base.validator.Id;
import com.am.server.common.base.validator.Save;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理
 * @author 阮雪峰
 * @date 2018/7/25 13:26
 */
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
     * @param page 分页
     * @param user 用户条件
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/25 15:56
     */
    @GetMapping("/list")
    public ResponseEntity list(Page<AdminUser> page, AdminUser user) {
        adminUserService.list(page, user);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * 新增
     * @param img 头像文件
     * @param user 用户信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/25 15:56
     */
    @WriteLog("新增")
    @PostMapping("/save")
    public ResponseEntity save(@Validated(Save.class) AdminUser user, @RequestParam(value = "img", required = false)MultipartFile img) {
        //检验邮箱是否存在
        if (adminUserService.isEmailExist(user)) {
            return new ResponseEntity<>(message.get(EMAIL_EXIST), HttpStatus.BAD_REQUEST);
        }

        if (adminUserService.isUsernameExist(user)) {
            return new ResponseEntity<>(message.get(USERNAME_EXIST), HttpStatus.BAD_REQUEST);
        }

        if (img == null || img.isEmpty()) {
            return new ResponseEntity<>(message.get(AVATAR_BLANK), HttpStatus.BAD_REQUEST);
        }
        adminUserService.save(user, img);

        return new ResponseEntity<>(message.get(SAVE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 修改
     * @param img 头像文件
     * @param user 用户信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/25 15:56
     */
    @WriteLog("修改")
    @PostMapping("/update")
    public ResponseEntity update(@Validated(Save.class) AdminUser user, @RequestParam(value = "img", required = false)MultipartFile img) {
        if (adminUserService.isEmailExist(user)) {
            return new ResponseEntity<>(message.get(EMAIL_EXIST), HttpStatus.BAD_REQUEST);
        }

        if (adminUserService.isUsernameExist(user)) {
            return new ResponseEntity<>(message.get(USERNAME_EXIST), HttpStatus.BAD_REQUEST);
        }

        adminUserService.update(user, img);

        return new ResponseEntity<>(message.get(SAVE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 删除
     * @param user 用户信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/25 16:24
     */
    @WriteLog("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@RequestHeader(Constant.TOKEN) String token, @Validated(Delete.class) AdminUser user) {
        if (user.getId().equals(Long.valueOf(JwtUtils.getSubject(token)))) {
            return new ResponseEntity<>(message.get(NOT_ALLOW_DELETE_YOURSELF), HttpStatus.BAD_REQUEST);
        }

        adminUserService.delete(user);

        return new ResponseEntity<>(message.get(DELETE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 重置密码
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
     * @param user 用户信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/30 12:49
     */
    @PostMapping("/isEmailExist")
    public ResponseEntity isEmailExist(AdminUser user) {
        Map<String, Boolean> map = new HashMap<>(1);
        map.put("isExist", adminUserService.isEmailExist(user));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * 用户名是否被使用
     * @param user user
     * @return org.springframework.http.ResponseEntity
     * @date 2019/4/10 9:23
     * @author 阮雪峰
     */
    @PostMapping("/isUsernameExist")
    public ResponseEntity isUsernameExist(AdminUser user){
        Map<String, Boolean> map = new HashMap<>(1);
        map.put("isExist", adminUserService.isUsernameExist(user));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * 角色id list
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
