package com.am.server.common.base.service;

/**
 * @author 阮雪峰
 * @date 2019年6月25日09:24:54
 */
public interface CommonService {

    /**
     * 获取登录用户id
     * @return Long
     */
    Long getLoginUserId();

    /**
     * 获取请求ip
     * @return String
     */
    String getRequestIp();
}
