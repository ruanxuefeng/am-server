package com.am.server.config.i18n.interceptor;

import com.am.server.config.i18n.exception.ParseLocaleValueException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Optional;

/**
 *  语言切换
 * @author 阮雪峰
 * @date 2018/7/25 11:43
 */
public class CustomLocaleChangeInterceptor extends HandlerInterceptorAdapter {

    private final Log logger = LogFactory.getLog(getClass());
    private static final String LANG = "lang";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String newLocale = Optional.ofNullable(request.getHeader(LANG)).orElse("zh");

        Optional<LocaleResolver> optional = Optional.ofNullable(RequestContextUtils.getLocaleResolver(request));
        optional.orElseThrow(() -> new IllegalStateException(
                "No LocaleResolver found: not in a DispatcherServlet request?"));

        optional.ifPresent(resolver -> {
            Locale locale = LocaleContextHolder.getLocale();
            Locale newOne = parseLocaleValue(newLocale);
            if (!locale.equals(newOne)) {
                try {
                    resolver.setLocale(request, response, newOne);
                } catch (ParseLocaleValueException ex) {
                    logger.error(ex.getMessage());
                }
            }
        });

        return true;
    }

    /**
     * 语言解析
     * @param lang lang
     * @return Locale
     * @author 阮雪峰
     * @date 2018/7/25 11:52
     */
    private Locale parseLocaleValue(String lang) {
        Locale locale;
        switch (lang) {
            case "zh":
                locale = Locale.CHINA;
                break;
            case "en":
                locale = Locale.US;
                break;
            default:
                throw new ParseLocaleValueException("local parse fail");
        }

        return locale;
    }
}
