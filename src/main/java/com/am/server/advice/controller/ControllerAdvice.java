package com.am.server.advice.controller;

import com.am.server.api.upload.exception.UploadFileException;
import com.am.server.api.user.exception.NoPermissionAccessException;
import com.am.server.api.user.exception.PasswordErrorException;
import com.am.server.api.user.exception.UserNotExistException;
import com.am.server.common.base.exception.NoTokenException;
import com.am.server.common.base.pojo.vo.MessageVO;
import com.am.server.common.base.service.Message;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import java.net.SocketTimeoutException;
import java.util.Objects;

/**
 * 通用校验放回校验
 *
 * @author 阮雪峰
 * @date 2018/9/4 15:49
 */
@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @Resource(name = "message")
    private Message<MessageVO> message;


    /**
     * token过期
     */
    private final static String TOKEN_EXPIRED = "exception.tokenExpired";
    /**
     * 没有权限访问
     */
    private final static String NO_PERMISSION_ACCESS = "exception.noPermissionAccess";

    /**
     * 请求没有携带token，提示登录
     */
    private final static String NO_TOKEN = "exception.noToken";

    /**
     * 服务器异常
     */
    private static final String SERVER_ERROR = "server.error";

    /**
     * 文件上传失败
     */
    private final static String FILE_UPLOAD_FAIL = "exception.file.upload.fail";

    private static final String USERNAME_DOES_NOT_EXIST = "login.username.does_not_exist";

    /**
     * 密码不正确
     */
    private static final String PASSWORD_ERROR = "login.password.error";

    /**
     * form表单验证错误
     *
     * @param e 错误信息
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author 阮雪峰
     * @date 2018/10/8 15:15
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageVO formBindException(BindException e) {
        log.error("表单验证错误", e);
        BindingResult bindingResult = e.getBindingResult();
        return message.get(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }

    /**
     * raw格式json数据校验
     *
     * @param e 错误信息
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author 阮雪峰
     * @date 2019/2/13 12:45
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageVO rawBindException(MethodArgumentNotValidException e) {
        log.error("raw格式json数据校验", e);
        BindingResult bindingResult = e.getBindingResult();
        return message.get(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }

    /**
     * 请求没有携带token，提示登录，401
     *
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/10/8 15:16
     */
    @ExceptionHandler(NoTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public MessageVO noTokenException(NoTokenException e) {
        log.error("token，提示登录，401", e);
        return message.get(NO_TOKEN);

    }

    /**
     * 用户不存在
     *
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/10/8 15:16
     */
    @ExceptionHandler(UserNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public MessageVO userNotExist( UserNotExistException e) {
        log.error("用户不存在", e);
        return message.get(USERNAME_DOES_NOT_EXIST);

    }

    /**
     * 密码不正确
     *
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/10/8 15:16
     */
    @ExceptionHandler(PasswordErrorException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public MessageVO passwordError( PasswordErrorException e) {
        log.error("密码不正确", e);
        return message.get(PASSWORD_ERROR);

    }

    /**
     * token过期或者不是正确的token，412
     *
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/10/8 15:16
     */
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public MessageVO tokenException( ExpiredJwtException e) {
        log.error("token过期或者不是正确的token，412", e);
        return message.get(TOKEN_EXPIRED);

    }

    /**
     * 没有权限访问，403
     *
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author 阮雪峰
     * @date 2018/10/8 15:25
     */
    @ExceptionHandler(NoPermissionAccessException.class)
    public ResponseEntity<MessageVO> noPermissionException(NoPermissionAccessException e) {
        log.error("没有权限访问，403", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message.get(NO_PERMISSION_ACCESS));

    }

    /**
     * 上传文件失败，521
     *
     * @param e 错误信息
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author 阮雪峰
     * @date 2018/10/8 15:25
     */
    @ExceptionHandler(UploadFileException.class)
    public ResponseEntity<MessageVO> noPermissionException(UploadFileException e) {
        log.error("上传文件失败，521", e);
        return ResponseEntity.status(521).body(message.get(FILE_UPLOAD_FAIL));

    }

    /**
     * 服务器异常，500
     *
     * @param e 错误信息
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author 阮雪峰
     * @date 2018/10/8 15:25
     */
    @ExceptionHandler(SocketTimeoutException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageVO noPermissionException(SocketTimeoutException e) {
        log.error("服务器异常，500", e);
        return message.get(SERVER_ERROR);

    }
}