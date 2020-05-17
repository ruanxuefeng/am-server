package com.am.server.api.user.repository;

import com.am.server.api.user.pojo.po.AdminUserDo;
import com.am.server.common.base.repository.BaseRepository;

import java.util.Optional;

/**
 * @author 阮雪峰
 * @date 2020年5月17日17:42:45
 */
public interface AdminUserRepository extends BaseRepository<AdminUserDo> {
    /**
     * 邮箱查询用户
     * @param email email
     * @return Optional<AdminUserPO>
     * @author 阮雪峰
     * @date 2019/1/16 12:21
     */
    Optional<AdminUserDo> findByEmail(String email);

    /**
     * 在修改的时候排除现有用户进行是否存在校验
     * @param id id
     * @param email email
     * @return Optional<AdminUserPO>
     * @date 2019/4/10 9:52
     * @author 阮雪峰
     */
    Optional<AdminUserDo> findByIdNotAndEmail(Long id, String email);

    /**
     * 用户名查询用户
     * @param username 用户名
     * @return Optional<AdminUserPO>
     * @date 2019/4/9 10:25
     * @author 阮雪峰
     */
    Optional<AdminUserDo> findByUsername(String username);

    /**
     * 在修改的时候排除现有用户进行是否存在校验
     * @param id id
     * @param username username
     * @return Optional<AdminUserPO>
     * @date 2019/4/10 9:52
     * @author 阮雪峰
     */
    Optional<AdminUserDo> findByIdNotAndUsername(Long id, String username);
}
