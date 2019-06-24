package com.am.server.api.admin.user.dao.jpa;

import com.am.server.api.admin.user.pojo.po.AdminUserPO;
import com.am.server.common.base.dao.BaseDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author 阮雪峰
 * @date 2019/1/16 12:07
 */
@Repository
public interface AdminUserDao extends BaseDao<AdminUserPO> {
    /**
     * 邮箱查询用户
     * @param email email
     * @return Optional<AdminUserPO>
     * @author 阮雪峰
     * @date 2019/1/16 12:21
     */
    Optional<AdminUserPO> findByEmail(String email);

    /**
     * 在修改的时候排除现有用户进行是否存在校验
     * @param id id
     * @param email email
     * @return Optional<AdminUserPO>
     * @date 2019/4/10 9:52
     * @author 阮雪峰
     */
    Optional<AdminUserPO> findByIdNotAndEmail(Long id, String email);

    /**
     * 用户名查询用户
     * @param username 用户名
     * @return Optional<AdminUserPO>
     * @date 2019/4/9 10:25
     * @author 阮雪峰
     */
    Optional<AdminUserPO> findByUsername(String username);

    /**
     * 在修改的时候排除现有用户进行是否存在校验
     * @param id id
     * @param username username
     * @return Optional<AdminUserPO>
     * @date 2019/4/10 9:52
     * @author 阮雪峰
     */
    Optional<AdminUserPO> findByIdNotAndUsername(Long id, String username);

    /**
     * 查询用户拥有的权限
     * @param id user id
     * @return List<String>
     * @author 阮雪峰
     * @date 2019/1/16 14:18
     */
    @Query(value = "select `key` from menu where id in (select menu from role_menu where role in (select role from user_role where user = ?1))", nativeQuery = true)
    List<String> findMenuList(Long id);

}
