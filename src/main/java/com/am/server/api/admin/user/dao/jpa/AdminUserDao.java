package com.am.server.api.admin.user.dao.jpa;

import com.am.server.api.admin.user.pojo.AdminUser;
import com.am.server.common.base.dao.BaseDao;
import com.am.server.common.base.enumerate.Gender;
import org.springframework.data.jpa.repository.Modifying;
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
public interface AdminUserDao extends BaseDao<AdminUser> {
    /**
     * 邮箱查询用户
     * @param email email
     * @return Optional<AdminUser>
     * @author 阮雪峰
     * @date 2019/1/16 12:21
     */
    Optional<AdminUser> findByEmail(String email);

    /**
     * 在修改的时候排除现有用户进行是否存在校验
     * @param id id
     * @param email email
     * @return Optional<AdminUser>
     * @date 2019/4/10 9:52
     * @author 阮雪峰
     */
    Optional<AdminUser> findByIdNotAndEmail(Long id, String email);

    /**
     * 用户名查询用户
     * @param username 用户名
     * @return Optional<AdminUser>
     * @date 2019/4/9 10:25
     * @author 阮雪峰
     */
    Optional<AdminUser> findByUsername(String username);

    /**
     * 在修改的时候排除现有用户进行是否存在校验
     * @param id id
     * @param username username
     * @return Optional<AdminUser>
     * @date 2019/4/10 9:52
     * @author 阮雪峰
     */
    Optional<AdminUser> findByIdNotAndUsername(Long id, String username);



    /**
     * 重置密码
     * @param id id
     * @param key key
     * @param password password
     * @author 阮雪峰
     * @date 2019/1/16 12:27
     */
    @Query("update AdminUser set key=?2,password=?3 where id=?1 ")
    @Modifying
    void resetPassword(Long id, String key, String password);

    /**
     * 修改基本信息
     * @param name name
     * @param gender gender
     * @param avatar avatar
     * @param id id
     * @author 阮雪峰
     * @date 2019/1/16 12:42
     */
    @Query(value = "update AdminUser set name=?1,gender=?2,avatar=?3 where id=?4")
    @Modifying
    void update(String name, Gender gender, String avatar, Long id);

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
