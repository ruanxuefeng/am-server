package com.am.server.api.user.dao.rdb;

import com.am.server.api.user.pojo.ao.AdminUserListAo;
import com.am.server.api.user.pojo.po.AdminUserDo;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * @author 阮雪峰
 * @date 2019/1/16 12:07
 */
public interface AdminUserDao {
    /**
     * 邮箱查询用户
     *
     * @param email email
     * @return Optional<AdminUserPO>
     * @author 阮雪峰
     * @date 2019/1/16 12:21
     */
    Optional<AdminUserDo> findByEmail(String email);

    /**
     * 在修改的时候排除现有用户进行是否存在校验
     *
     * @param id    id
     * @param email email
     * @return Optional<AdminUserPO>
     * @date 2019/4/10 9:52
     * @author 阮雪峰
     */
    Optional<AdminUserDo> findByIdNotAndEmail(Long id, String email);

    /**
     * 用户名查询用户
     *
     * @param username 用户名
     * @return Optional<AdminUserPO>
     * @date 2019/4/9 10:25
     * @author 阮雪峰
     */
    Optional<AdminUserDo> findByUsername(String username);

    /**
     * 在修改的时候排除现有用户进行是否存在校验
     *
     * @param id       id
     * @param username username
     * @return Optional<AdminUserPO>
     * @date 2019/4/10 9:52
     * @author 阮雪峰
     */
    Optional<AdminUserDo> findByIdNotAndUsername(Long id, String username);

    /**
     * 通过主键查询
     *
     * @param id 主键
     * @return Optional<AdminUserDo>
     */
    Optional<AdminUserDo> findById(Long id);

    /**
     * 分页查询
     *
     * @param listQuery 查询条件
     * @return Page<AdminUserDo>
     */
    Page<AdminUserDo> findAll(AdminUserListAo listQuery);

    /**
     * 保存
     *
     * @param user user
     */
    void save(AdminUserDo user);

    /**
     * delete by id
     *
     * @param id 主键
     */
    void deleteById(Long id);
}
