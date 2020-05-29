package com.am.server.common.base.service;

import com.am.server.common.base.pojo.po.BaseDo;

/**
 * @author 阮雪峰
 * @date 2019年6月25日09:24:54
 */
public interface CommonService {

    /**
     * 获取登录用户id
     *
     * @return Long
     */
    Long getLoginUserId();

    /**
     * 获取请求ip
     *
     * @return String
     */
    String getRequestIp();

    /**
     * 在新增或修改之前执行
     * 新增时会设置id、createdById、createdTime、updatedBy、updatedTime
     * 修改会更新updatedBy、updatedTime
     *
     * @param entity entity
     */
    void beforeSave(BaseDo entity);

    /**
     * 当前用户是否是超级用户
     *
     * @return boolean
     */
    boolean isSupperUser();

}
