package com.am.server.advice.controller;

import com.am.server.api.upload.exception.UploadFileException;
import com.am.server.api.user.exception.NoPermissionAccessException;
import com.am.server.api.user.exception.PasswordErrorException;
import com.am.server.api.user.exception.UserNotExistException;
import com.am.server.common.base.exception.NoTokenException;
import com.am.server.common.base.pojo.vo.MessageVO;
import com.am.server.common.base.service.Message;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    /**
     * 方法参数类型错误
     */
    private final static String PARAMETER_TYPE_ERROR = "exception.parameter.type";
    /**
     * json参数类型错误
     */
    private final static String JSON_PARAMETER_TYPE_ERROR = "exception.json.parameter.type";

    private static final String VALIDATE_FAIL = "login.validate.fail";

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
        BindingResult bindingResult = e.getBindingResult();
        log.error("表单验证错误, 参数：{}", bindingResult.getFieldErrors());
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
        log.error("raw格式json数据校验, 参数：{}", e.getBindingResult().getFieldErrors());
        BindingResult bindingResult = e.getBindingResult();
        return message.get(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }

    /**
     * 空结果集异常
     *
     * @param e 错误信息
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author 阮雪峰
     * @date 2019/2/13 12:45
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageVO emptyResultDataAccessException(EmptyResultDataAccessException e) {
        log.error(e.toString(),e);
        return new MessageVO(e.getMessage());
    }

    /**
     * 参数转换异常
     *
     * @param e 错误信息
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author 阮雪峰
     * @date 2019/2/13 12:45
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageVO methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String methodName = Objects.requireNonNull(e.getParameter().getMethod()).getName();
        String parameterName = e.getName();
        String parameterType = e.getParameter().getParameterType().getSimpleName();
        String actualParameterType = Objects.requireNonNull(e.getValue()).getClass().getSimpleName();
        String actualParameterValue = Objects.requireNonNull(e.getValue()).toString();
        log.error("方法：{}，参数：{}，类型为：{}，实际类型为：{}，参数值：{}",
                methodName,
                parameterName,
                parameterType,
                actualParameterType,
                actualParameterValue);

        MessageVO messageVO = message.get(PARAMETER_TYPE_ERROR);
        messageVO.setMessage(
                String.format(messageVO.getMessage(),
                        methodName,
                        parameterName,
                        parameterType,
                        actualParameterType,
                        actualParameterValue)
        );
        return messageVO;
    }

    /**
     * 参数转换异常
     *
     * @param e 错误信息
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author 阮雪峰
     * @date 2019/2/13 12:45
     */
    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageVO invalidFormatException(InvalidFormatException e) {
        String parameterName = e.getPathReference();
        String parameterType = e.getTargetType().getSimpleName();
        String actualParameterType = Objects.requireNonNull(e.getValue()).getClass().getSimpleName();
        String actualParameterValue = Objects.requireNonNull(e.getValue()).toString();
        log.error("参数：{}，类型为：{}，实际类型为：{}，参数值：{}",
                parameterName,
                parameterType,
                actualParameterType,
                actualParameterValue);

        MessageVO messageVO = message.get(JSON_PARAMETER_TYPE_ERROR);
        messageVO.setMessage(
                String.format(messageVO.getMessage(),
                        parameterName,
                        parameterType,
                        actualParameterType,
                        actualParameterValue)
        );
        return messageVO;
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
    public MessageVO noTokenException() {
        log.error("token，提示登录，401");
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
    public MessageVO userNotExist() {
        log.error("用户登录校验失败：用户不存在");
        return message.get(VALIDATE_FAIL);

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
    public MessageVO passwordError() {
        log.error("用户登录校验失败：密码不正确");
        return message.get(VALIDATE_FAIL);

    }

    /**
     * token过期或者不是正确的token，412
     *
     * @return org.springframework.http.ResponseEntity
     * @author 阮雪峰
     * @date 2018/10/8 15:16
     */
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public MessageVO tokenException() {
        log.error("token过期或者不是正确的token，412");
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
    public ResponseEntity<MessageVO> noPermissionException() {
        log.error("没有权限访问，403");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message.get(NO_PERMISSION_ACCESS));

    }

    /**
     * @param e HttpRequestMethodNotSupportedException
     * @return MessageVO
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageVO httpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.error("请求方法不支持，支持方法：{}，请求方法：{}", e.getSupportedHttpMethods(), e.getMethod());
        return message.get(SERVER_ERROR);
    }

    /**
     * 上传文件失败，521
     *
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author 阮雪峰
     * @date 2018/10/8 15:25
     */
    @ExceptionHandler(UploadFileException.class)
    public ResponseEntity<MessageVO> uploadFileFailException() {
        log.error("上传文件失败，521");
        return ResponseEntity.status(521).body(message.get(FILE_UPLOAD_FAIL));

    }

    /**
     * 服务器异常，500
     *
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author 阮雪峰
     * @date 2018/10/8 15:25
     */
    @ExceptionHandler(SocketTimeoutException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageVO serverException() {
        log.error("服务器异常，500");
        return message.get(SERVER_ERROR);
    }

}
