package com.am.server.common.base.controller;

import com.am.server.common.base.pojo.vo.MessageVO;
import com.am.server.common.base.service.Message;

import javax.annotation.Resource;

/**
 * 提供基本常量
 *
 * @author 阮雪峰
 * @date 2018/8/3 16:46
 */
public class BaseController {
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

    /**
     * 缺少删除主键
     */
    protected static final String COMMON_DELETE_PRIMARY_KEY_NULL = "common.delete.primaryKey.null";
    /**
     * 缺少操作主键
     */
    protected static final String COMMON_OPERATE_PRIMARY_KEY_NULL = "common.operate.primaryKey.null";

    @Resource(name = "message")
    protected Message<MessageVO> message;

}
