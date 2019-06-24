package com.am.server.common.base.service.impl;

import com.am.server.common.base.pojo.vo.MessageVO;
import com.am.server.common.base.service.Message;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 阮雪峰
 * @date 2018/10/29 9:05
 */
public class NormalMessageImpl implements Message<MessageVO> {
    @Override
    public MessageVO get(String message) {
        return new MessageVO(message);
    }
}
