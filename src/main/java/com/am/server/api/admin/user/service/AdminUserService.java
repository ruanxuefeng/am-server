package com.am.server.api.admin.user.service;

import com.am.server.api.admin.user.pojo.AdminUser;
import com.am.server.api.admin.user.pojo.param.SaveAdminUserAO;
import com.am.server.api.admin.user.pojo.param.ListQuery;
import com.am.server.api.admin.user.pojo.param.LoginQuery;
import com.am.server.api.admin.user.pojo.param.UpdateAdminUserAO;
import com.am.server.api.admin.user.pojo.vo.AdminUserListVO;
import com.am.server.common.base.entity.PageVO;
import com.am.server.common.base.service.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 阮雪峰
 * @date 2018/7/24 16:46
 */
public interface AdminUserService extends BaseService {
    /**
     * 登录
     *
     * @param user 用户信息
     * @return com.am.server.api.admin.user.pojo.AdminUser
     * @author 阮雪峰
     * @date 2018/8/23 8:36
     */
    AdminUser login(LoginQuery query);

    /**
     * 获取用户信息
     *
     * @param id 用户信息
     * @return com.am.server.api.admin.user.pojo.AdminUser
     * @author 阮雪峰
     * @date 2018/8/23 8:36
     */
    AdminUser info(Long id);

    /**
     * 分页
     *
     * @param listQuery listQuery
     * @author 阮雪峰
     * @date 2018/7/25 15:51
     * @return PageVO<AdminUserListVO>
     */
    PageVO<AdminUserListVO> list(ListQuery listQuery);

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
     * @param user 用户信息
     * @author 阮雪峰
     * @date 2018/7/25 16:23
     */
    void delete(AdminUser user);

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
     * @param user 用户信息
     * @author 阮雪峰
     * @date 2018/7/25 17:03
     */
    void resetPassword(AdminUser user);

    /**
     * 修改角色
     *
     * @param user 用户信息
     * @date 2018/7/27 15:56
     */
    void updateRole(AdminUser user);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @author 阮雪峰
     * @date 2018/8/3 16:55
     */
    void update(UpdateAdminUserAO user);

    /**
     * findById
     *
     * @param id id
     * @return com.am.server.api.admin.user.pojo.AdminUser
     * @author 阮雪峰
     * @date 2019/2/14 16:04
     */
    AdminUser findById(Long id);

    /**
     * 查询角色id list
     *
     * @param user user
     * @return java.util.List<java.lang.Long>
     * @date 2019/4/10 9:24
     * @author 阮雪峰
     */
    List<Long> findRoleIdList(AdminUser user);

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
     * @param id    id
     * @return Boolean
     */
    Boolean isUsernameExistWithId(String username, Long id);
}
