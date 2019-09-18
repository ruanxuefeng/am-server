package com.am.server.api.user.dao.rdb;

import com.am.server.api.user.pojo.po.AdminUserDO;
import com.am.server.common.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * @author 阮雪峰
 * @date 2019/1/16 12:07
 */
@Repository
public interface AdminUserDAO extends BaseDao<AdminUserDO> {
    /**
     * 邮箱查询用户
     * @param email email
     * @return Optional<AdminUserPO>
     * @author 阮雪峰
     * @date 2019/1/16 12:21
     */
    Optional<AdminUserDO> findByEmail(String email);

    /**
     * 在修改的时候排除现有用户进行是否存在校验
     * @param id id
     * @param email email
     * @return Optional<AdminUserPO>
     * @date 2019/4/10 9:52
     * @author 阮雪峰
     */
    Optional<AdminUserDO> findByIdNotAndEmail(Long id, String email);

    /**
     * 用户名查询用户
     * @param username 用户名
     * @return Optional<AdminUserPO>
     * @date 2019/4/9 10:25
     * @author 阮雪峰
     */
    Optional<AdminUserDO> findByUsername(String username);

    /**
     * 在修改的时候排除现有用户进行是否存在校验
     * @param id id
     * @param username username
     * @return Optional<AdminUserPO>
     * @date 2019/4/10 9:52
     * @author 阮雪峰
     */
    Optional<AdminUserDO> findByIdNotAndUsername(Long id, String username);

}