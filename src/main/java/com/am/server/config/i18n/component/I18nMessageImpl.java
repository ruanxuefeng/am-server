package com.am.server.config.i18n.component;

import com.am.server.common.base.service.Message;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *  消息基类
 * @author 阮雪峰
 * @date 2018/8/27 10:01
 */
public class I18nMessageImpl implements Message<Map<String, String>> {
    @Resource(name = "messageSource")
    private MessageSource source;

    private static final Object OBJECT = null;

    /**
     * 获取国际化提示信息
     * @param key 获取提示键值
     * @param args 不知道啥用
     */
    private Map<String, String> message(String key, Object... args) {
        // 消息的参数化和国际化配置
        Locale locale = LocaleContextHolder.getLocale();
        Map<String, String> map = new HashMap<>(1);
        map.put(MESSAGE, source.getMessage(key, args, locale));
        return map;
    }

    /**
     * 获取国际化提示信息
     * @param key 获取提示键值
     * @author 阮雪峰
     * @date 2018/7/25 10:57
     */
    @Override
    public Map<String, String> get(String key) {
        return message(key, OBJECT);
    }
}
