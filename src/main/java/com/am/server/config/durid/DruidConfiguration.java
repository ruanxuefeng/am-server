package com.am.server.config.durid;

import com.alibaba.druid.support.http.WebStatFilter;
import com.am.server.api.permission.annotation.Permission;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * sql监控配置
 *
 * @author 阮雪峰
 * @date 2018/9/5 9:10
 */
@Permission(value = "druid", name = "SQL监控", sort = 15)
@Configuration
public class DruidConfiguration {

    @Bean
    public ServletRegistrationBean<AmStatViewServlet> druidServlet() {

        ServletRegistrationBean<AmStatViewServlet> servletRegistrationBean
                = new ServletRegistrationBean<>(new AmStatViewServlet(), "/druid/*");
        // IP白名单
        servletRegistrationBean.addInitParameter("allow", "*");
        // IP黑名单(共同存在时，deny优先于allow)
        servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
        //是否能够重置数据 禁用HTML页面上的“Reset All”功能
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}
