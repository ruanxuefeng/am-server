package com.am.server.api.admin.user.service;

import com.am.server.api.admin.user.pojo.param.*;
import com.am.server.api.admin.user.pojo.po.AdminUserPO;
import com.am.server.api.admin.user.pojo.vo.AdminUserListVO;
import com.am.server.api.admin.user.pojo.vo.LoginUserInfoVO;
import com.am.server.api.admin.user.pojo.vo.UserInfoVO;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.base.service.BaseService;

import java.util.List;

/**
 * @author 阮雪峰
 * @date 2018/7/24 16:46
 */
public interface AdminUserService extends BaseService {
    /**
     * 登录
     *
     * @param query 用户信息
     * @return com.am.server.api.admin.user.pojo.AdminUserPO
     * @author 阮雪峰
     * @date 2018/8/23 8:36
     */
    LoginUserInfoVO login(LoginAO query);

    /**
     * 获取用户信息
     *
     * @param id 用户信息
     * @return com.am.server.api.admin.user.pojo.AdminUserPO
     * @author 阮雪峰
     * @date 2018/8/23 8:36
     */
    UserInfoVO info(Long id);

    /**
     * 分页
     *
     * @param list list
     * @return PageVO<AdminUserListVO>
     * @author 阮雪峰
     * @date 2018/7/25 15:51
     */
    PageVO<AdminUserListVO> list(ListAO list);

    /**
     * 新增
     *
     * @param user 用户信息
     * @author 阮雪峰
     * @date 2018/7/25 15:51
     */
    void save(SaveAdminUserAO user);

    /**
     * 删除
     *
     * @param id 用户id
     * @author 阮雪峰
     * @date 2018/7/25 16:23
     */
    void delete(Long id);

    /**
     * 邮箱是否已被使用
     *
     * @param email email
     * @return boolean
     * @author 阮雪峰
     * @date 2018/7/25 16:39
     */
    Boolean isEmailExist(String email);

    /**
     * 重置密码
     *
     * @param id 用户主键
     * @author 阮雪峰
     * @date 2018/7/25 17:03
     */
    void resetPassword(Long id);

    /**
     * 修改角色
     *
     * @param id         用户id
     * @param roleIdList 与用户要关联的角色的id
     */
    void updateRole(Long id, List<Long> roleIdList);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @author 阮雪峰
     * @date 2018/8/3 16:55
     */
    void update(UpdateAdminUserAO user);

    /**
     * 更新登录用户信息
     *
     * @param user 用户信息
     * @author 阮雪峰
     * @date 2018/8/3 16:55
     */
    void update(UpdateUserInfoAO user);

    /**
     * findById
     *
     * @param id id
     * @return com.am.server.api.admin.user.pojo.AdminUserPO
     * @author 阮雪峰
     * @date 2019/2/14 16:04
     */
    AdminUserPO findById(Long id);

    /**
     * 查询角色id list
     *
     * @param id id
     * @return java.util.List<java.lang.Long>
     * @date 2019/4/10 9:24
     * @author 阮雪峰
     */
    List<Long> findRoleIdList(Long id);

    /**
     * 在所有的用户中，判断用户名是否存在
     *
     * @param username username
     * @return Boolean
     */
    Boolean isUsernameExist(String username);

    /**
     * 排除当前用户后，查询邮箱是否存在
     *
     * @param email email
     * @param id    id
     * @return Boolean
     */
    Boolean isEmailExistWithId(String email, Long id);

    /**
     * 排除当前用户后，查询用户名是否存在
     *
     * @param username username
     * @param id       id
     * @return Boolean
     */
    Boolean isUsernameExistWithId(String username, Long id);
}
