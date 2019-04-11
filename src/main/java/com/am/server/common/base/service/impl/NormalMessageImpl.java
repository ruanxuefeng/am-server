package com.am.server.common.base.service.impl;

import com.am.server.common.base.service.Message;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 阮雪峰
 * @date 2018/10/29 9:05
 */
public class NormalMessageImpl implements Message<Map<String, String>> {
    @Override
    public Map<String, String> get(String message) {
        Map<String, String> map = new HashMap<>(1);
        map.put(MESSAGE, message);
        return map;
    }
}
