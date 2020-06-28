package com.am.server.api.user.controller;

import cn.hutool.core.util.StrUtil;
import com.am.server.api.user.pojo.ao.LoginAo;
import com.am.server.api.user.pojo.ao.UpdateUserInfoAo;
import com.am.server.api.user.pojo.vo.LoginUserInfoVo;
import com.am.server.api.user.pojo.vo.UserInfoVo;
import com.am.server.api.user.service.AdminUserService;
import com.am.server.api.user.service.UserPermissionCacheService;
import com.am.server.common.base.controller.BaseController;
import com.am.server.common.base.pojo.vo.MessageVO;
import com.am.server.common.base.service.Message;
import com.am.server.common.base.validator.Login;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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

    private static final String NO_AVATAR = "user.avatar.blank";

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
    public ResponseEntity<LoginUserInfoVo> login(@Validated(Login.class) @RequestBody @ApiParam(name = "登录对象", value = "传入json格式", required = true) LoginAo query) {
        return new ResponseEntity<>(adminUserService.login(query), HttpStatus.OK);
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
    public ResponseEntity<UserInfoVo> info(@RequestHeader(Constant.TOKEN) String token) {
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
    public ResponseEntity<MessageVO> updateUserInfo(@RequestHeader(Constant.TOKEN) String token, @Validated UpdateUserInfoAo user) {
        user.setId(Long.valueOf(JwtUtils.getSubject(token)));
        if (StrUtil.isEmpty(user.getAvatar()) && user.getImg() == null) {
            return ResponseEntity.badRequest().body(message.get(NO_AVATAR));
        }
        adminUserService.update(user);
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
    public ResponseEntity<MessageVO> logout(@RequestHeader(Constant.TOKEN) String token) {
        userPermissionCacheService.remove(Long.valueOf(JwtUtils.getSubject(token)));
        return ResponseEntity.ok(message.get(SUCCESS));
    }
}
