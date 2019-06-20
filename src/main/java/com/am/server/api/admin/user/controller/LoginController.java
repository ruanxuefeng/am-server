package com.am.server.api.admin.user.controller;

import com.am.server.api.admin.user.pojo.AdminUser;
import com.am.server.api.admin.user.pojo.param.LoginQuery;
import com.am.server.api.admin.user.service.AdminUserService;
import com.am.server.api.admin.user.service.UserPermissionCacheService;
import com.am.server.common.base.controller.BaseController;
import com.am.server.common.base.validator.Login;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.DesUtils;
import com.am.server.common.util.JwtUtils;
import io.swagger.annotations.*;
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


import java.util.Base64;
import java.util.Optional;

/**
 * 登录及登录用户相关
 *
 * @author 阮雪峰
 * @date 2018/7/24 9:51
 */
@Api(tags = "用户登录相关接口")
@RestController
@RequestMapping(Constant.ADMIN_ROOT)
public class LoginController extends BaseController {

    private static final String EMAIL_DOES_NOT_EXIST = "login.email.does_not_exist";
    private static final String PASSWORD_ERROR = "login.password.error";

    private final AdminUserService adminUserService;

    private final UserPermissionCacheService userPermissionCacheService;

    public LoginController(AdminUserService adminUserService, UserPermissionCacheService userPermissionCacheService) {
        this.adminUserService = adminUserService;
        this.userPermissionCacheService = userPermissionCacheService;
    }

    /**
     * 登录
     *
     * @param query 用户
     * @return org.springframework.http.CustomResponseEntity
     * @author 阮雪峰
     * @date 2018/7/24 10:40
     */
    @ApiOperation(value = "登录", notes = "登录接口")
    @PostMapping("/login")
    public ResponseEntity login(@Validated(Login.class) @RequestBody @ApiParam(name="登录对象",value="传入json格式",required=true) LoginQuery query) {

        Optional<AdminUser> loginUserOption = Optional.ofNullable(adminUserService.login(query));
        if (loginUserOption.isPresent()) {
            Optional<ResponseEntity> result = loginUserOption.filter(loginUser -> {
                //校验密码是否一样
                String inputPassword = new String(Base64.getDecoder().decode(query.getPassword().getBytes()));
                String userPassword = DesUtils.decrypt(loginUser.getPassword(), loginUser.getKey());
                return inputPassword.equals(userPassword);
            }).map(loginUser -> {
                //移除密码信息
                loginUser.setToken(JwtUtils.sign(loginUser.getId().toString()));
                return new ResponseEntity<>(loginUser, HttpStatus.OK);
            });
            return result.orElse(new ResponseEntity<>(message.get(PASSWORD_ERROR), HttpStatus.NOT_ACCEPTABLE));
        } else {
            return new ResponseEntity<>(message.get(EMAIL_DOES_NOT_EXIST), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * 获取用户信息
     *
     * @param token 登录凭证
     * @return ResponseEntity
     * @author 阮雪峰
     * @date 2018/7/25 9:08
     */
    @ApiOperation(value = "获取登录用户信息")
    @GetMapping("/user/info")
    public ResponseEntity info(@RequestHeader(Constant.TOKEN) String token) {
        return new ResponseEntity<>(adminUserService.info(Long.valueOf(JwtUtils.getSubject(token))), HttpStatus.OK);
    }

    /**
     * 修改登录用户个人信息
     *
     * @param token 登录凭证
     * @param user  用户信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/8/3 16:47
     */
    @PostMapping("/user/update/info")
    public ResponseEntity updateUserInfo(@RequestHeader(Constant.TOKEN) String token, AdminUser user, @RequestParam(value = "img", required = false) MultipartFile img) {
        user.setId(Long.valueOf(JwtUtils.getSubject(token)));
//        adminUserService.update(user, img);
        return ResponseEntity.ok(message.get(UPDATE_SUCCESS));
    }

    /**
     * 退出登录
     *
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2019/1/25 9:31
     */
    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader(Constant.TOKEN) String token) {
        userPermissionCacheService.remove(Long.valueOf(JwtUtils.getSubject(token)));
        return ResponseEntity.ok(SUCCESS);
    }
}
