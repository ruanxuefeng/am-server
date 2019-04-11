package com.am.server.common.base.controller;

import com.am.server.common.base.service.Message;
import com.am.server.config.i18n.component.I18nMessageImpl;

import javax.annotation.Resource;
import java.util.Map;


/**
 *  提供基本常量
 * @author 阮雪峰
 * @date 2018/8/3 16:46
 */
public class BaseController  {
    /**
     * 操作成功!
     */
    protected static final String SUCCESS = "common.success";
    /**
     * 新增成功
     */
    protected static final String SAVE_SUCCESS = "common.save.success";

    /**
     * 修改成功
     */
    protected static final String UPDATE_SUCCESS = "common.update.success";
    /**
     * 删除成功
     */

    protected static final String DELETE_SUCCESS = "common.delete.success";

    @Resource(name = "message")
    protected Message<Map<String, String>> message;

}
