package com.am.server.config.i18n.exception;

/**
 *  解析语言失败异常
 * @author 阮雪峰
 * @date 2018/7/25 11:52
 */
public class ParseLocaleValueException extends RuntimeException {
    public ParseLocaleValueException(String message) {
        super(message);
    }
}
