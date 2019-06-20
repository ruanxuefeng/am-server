package com.am.server.api.admin.user.pojo.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * admin_user
 * @author 阮雪峰
 * @date 2019年6月20日15:03:11
 */
public class AdminUserPO {
    @javax.persistence.Id
    @Column(length = 18)
    private Long id;

    @Column(length = 18, updatable = false)
    private Long creator;
}
