package com.am.server.api.user.service;

import com.am.server.api.user.pojo.ao.*;
import com.am.server.api.user.pojo.vo.AdminUserListVo;
import com.am.server.api.user.pojo.vo.LoginUserInfoVo;
import com.am.server.api.user.pojo.vo.UserInfoVo;
import com.am.server.common.base.pojo.vo.PageVO;

import java.util.List;

/**
 * @author 阮雪峰
 * @date 2018/7/24 16:46
 */
public interface AdminUserService {
    /**
     * 登录
     *
     * @param query 用户信息
     * @return com.am.server.api.admin.user.pojo.AdminUserPO
     * @author 阮雪峰
     * @date 2018/8/23 8:36
     */
    LoginUserInfoVo login(LoginAo query);

    /**
     * 获取用户信息
     *
     * @param id 用户信息
     * @return com.am.server.api.admin.user.pojo.AdminUserPO
     * @author 阮雪峰
     * @date 2018/8/23 8:36
     */
    UserInfoVo info(Long id);

    /**
     * 分页
     *
     * @param list list
     * @return PageVO<AdminUserListVO>
     * @author 阮雪峰
     * @date 2018/7/25 15:51
     */
    PageVO<AdminUserListVo> list(AdminUserListAo list);

    /**
     * 新增
     *
     * @param user 用户信息
     * @author 阮雪峰
     * @date 2018/7/25 15:51
     */
    void save(SaveAdminUserAo user);

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
     * 管理员重置用户密码
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
     * 管理员更新用户基本信息
     *
     * @param user 用户信息
     * @author 阮雪峰
     * @date 2018/8/3 16:55
     */
    void update(UpdateAdminUserAo user);

    /**
     * 用户修改自己的基本信息
     *
     * @param user 用户信息
     * @author 阮雪峰
     * @date 2018/8/3 16:55
     */
    void update(UpdateUserInfoAo user);

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
