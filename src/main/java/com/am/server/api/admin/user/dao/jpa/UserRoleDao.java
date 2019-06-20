package com.am.server.api.admin.user.dao.jpa;

import com.am.server.api.admin.user.pojo.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author 阮雪峰
 * @date 2019/1/16 13:10
 */
@Repository
public interface UserRoleDao extends JpaRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole> {
    /**
     * 删除用户角色关联
     * @param user user
     * @author 阮雪峰
     * @date 2019/2/14 16:08
     */
    void deleteByUser(Long user);
}
