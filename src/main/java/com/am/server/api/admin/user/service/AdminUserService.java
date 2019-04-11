package com.am.server.api.admin.user.service;

import com.am.server.api.admin.user.entity.AdminUser;
import com.am.server.common.base.page.Page;
import com.am.server.common.base.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *
 * @author 阮雪峰
 * @date 2018/7/24 16:46
 */
public interface AdminUserService extends BaseService {
    /**
     * 登录
     * @param user 用户信息
     * @return com.am.server.api.admin.user.entity.AdminUser
     * @author 阮雪峰
     * @date 2018/8/23 8:36
     */
    AdminUser login(AdminUser user);

    /**
     * 获取用户信息
     * @param id 用户信息
     * @return com.am.server.api.admin.user.entity.AdminUser
     * @author 阮雪峰
     * @date 2018/8/23 8:36
     */
    AdminUser info(Long id);

    /**
     * 分页
     * @param page 分页
     * @param user 用户信息
     * @author 阮雪峰
     * @date 2018/7/25 15:51
     */
    void list(Page<AdminUser> page, AdminUser user);

    /**
     * 新增
     * @param user 用户信息
     * @param img
     * @author 阮雪峰
     * @date 2018/7/25 15:51
     */
    void save(AdminUser user, MultipartFile img);

    /**
     * 删除
     * @param user 用户信息
     * @author 阮雪峰
     * @date 2018/7/25 16:23
     */
    void delete(AdminUser user);

    /**
     * 邮箱是否已被使用
     * @param user 用户信息
     * @return boolean
     * @author 阮雪峰
     * @date 2018/7/25 16:39
     */
    boolean isEmailExist(AdminUser user);

    /**
     * 重置密码
     * @param user 用户信息
     * @author 阮雪峰
     * @date 2018/7/25 17:03
     */
    void resetPassword(AdminUser user);

    /**
     * 修改角色
     * @param user 用户信息
     * @date 2018/7/27 15:56
     */
    void updateRole(AdminUser user);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @param img
     * @author 阮雪峰
     * @date 2018/8/3 16:55
     */
    void update(AdminUser user, MultipartFile img);

    /**
     *findById
     * @param id id
     * @return com.am.server.api.admin.user.entity.AdminUser
     * @author 阮雪峰
     * @date 2019/2/14 16:04
     */
    AdminUser findById(Long id);

    /**
     * 查询角色id list
     * @param user user
     * @return java.util.List<java.lang.Long>
     * @date 2019/4/10 9:24
     * @author 阮雪峰
     */
    List<Long> findRoleIdList(AdminUser user);

    Boolean isUsernameExist(AdminUser user);
}
