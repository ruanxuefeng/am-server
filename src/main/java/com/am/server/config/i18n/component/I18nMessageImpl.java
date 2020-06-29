package com.am.server.config.i18n.component;

import com.am.server.common.base.pojo.vo.MessageVO;
import com.am.server.common.base.service.Message;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.annotation.Resource;
import java.util.Locale;

/**
 *  消息基类
 * @author 阮雪峰
 * @date 2018/8/27 10:01
 */
public class I18nMessageImpl implements Message<MessageVO> {
    @Resource(name = "messageSource")
    private MessageSource source;

    private static final Object OBJECT = null;
    private static final String  DEFAULT_MESSAGE = "common.default.message";

    /**
     * 获取国际化提示信息
     * @param key 获取提示键值
     * @param args 不知道啥用
     */
    private MessageVO message(String key, Object... args) {
        // 消息的参数化和国际化配置
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return new MessageVO(source.getMessage(key, args, locale));
        } catch (NoSuchMessageException e) {
            return getDefaultMessage();
        }
    }

    /**
     * 获取国际化提示信息
     * @param key 获取提示键值
     * @author 阮雪峰
     * @date 2018/7/25 10:57
     */
    @Override
    public MessageVO get(String key) {
        return message(key, OBJECT);
    }

    private MessageVO getDefaultMessage() {
        Locale locale = LocaleContextHolder.getLocale();
        return new MessageVO(source.getMessage(DEFAULT_MESSAGE, null, locale));
    }
}
