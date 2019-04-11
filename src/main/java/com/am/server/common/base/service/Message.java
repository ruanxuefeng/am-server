package com.am.server.common.base.service;

/**
 * 系统消息类接口
 * @author 阮雪峰
 * @date 2018/10/29 8:59
 */
public interface Message<T> {

    String MESSAGE = "message";

    /**
     * 获取消息
     * @param message 消息
     * @return T 返回值
     * @author 阮雪峰
     * @date 2018/10/29 9:01
     */
    T get(String message);
}
