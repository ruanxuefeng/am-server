package com.am.server.advice.controller;

import com.am.server.api.admin.upload.exception.UploadFileException;
import com.am.server.api.admin.user.exception.NoPermissionAccessException;
import com.am.server.api.admin.user.exception.NoTokenException;
import com.am.server.api.admin.user.exception.TokenExpiredException;
import com.am.server.common.base.service.Message;
import com.am.server.config.i18n.component.I18nMessageImpl;
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
import java.util.Map;
import java.util.Objects;

/**
 * 通用校验放回校验
 * @author 阮雪峰
 * @date 2018/9/4 15:49
 */
@Slf4j
@RestControllerAdvice
public class ControllerValidateAdvice {

    @Resource(name = "message")
    private Message<Map<String, String>> message;


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

    private final static String ERROR_TITLE = "error";

    /**
     * form表单验证错误
     * @param e 错误信息
     * @return java.util.Map<java.lang.String , java.lang.String>
     * @author 阮雪峰
     * @date 2018/10/8 15:15
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> formBindException(BindException e) {
        log.error(ERROR_TITLE, e);
        BindingResult bindingResult = e.getBindingResult();
        return message.get(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }

    /**
     * raw格式json数据校验
     * @param e 错误信息
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @author 阮雪峰
     * @date 2019/2/13 12:45
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> rawBindException(MethodArgumentNotValidException e) {
        log.error(ERROR_TITLE, e);
        BindingResult bindingResult = e.getBindingResult();
        return message.get(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }

    /**
     * 请求没有携带token，提示登录，401
     * @param e 错误信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/10/8 15:16
     */
    @ExceptionHandler(NoTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> noTokenException(NoTokenException e) {
        log.error(ERROR_TITLE, e);
        return message.get(NO_TOKEN);

    }

    /**
     * token过期或者不是正确的token，412
     * @param e 错误信息
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/10/8 15:16
     */
    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public Map<String, String> tokenException(TokenExpiredException e) {
        log.error(ERROR_TITLE, e);
        return message.get(TOKEN_EXPIRED);

    }

    /**
     * 没有权限访问，403
     * @param e 错误信息
     * @return java.util.Map<java.lang.String , java.lang.String>
     * @author 阮雪峰
     * @date 2018/10/8 15:25
     */
    @ExceptionHandler(NoPermissionAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> noPermissionException(NoPermissionAccessException e) {
        log.error(ERROR_TITLE, e);
        return message.get(NO_PERMISSION_ACCESS);

    }

    /**
     * 上传文件失败，521
     * @param e 错误信息
     * @return java.util.Map<java.lang.String , java.lang.String>
     * @author 阮雪峰
     * @date 2018/10/8 15:25
     */
    @ExceptionHandler(UploadFileException.class)
    public ResponseEntity noPermissionException(UploadFileException e) {
        log.error(ERROR_TITLE, e);
        return ResponseEntity.status(521).body(message.get(FILE_UPLOAD_FAIL));

    }

    /**
     * 服务器异常，500
     * @param e 错误信息
     * @return java.util.Map<java.lang.String , java.lang.String>
     * @author 阮雪峰
     * @date 2018/10/8 15:25
     */
    @ExceptionHandler(SocketTimeoutException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> noPermissionException(SocketTimeoutException e) {
        log.error(ERROR_TITLE, e);
        return message.get(SERVER_ERROR);

    }
}
