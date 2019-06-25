package com.am.server.api.admin.role.dao.jpa;

import com.am.server.api.admin.role.pojo.po.RolePO;
import com.am.server.common.base.dao.BaseDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author 阮雪峰
 * @date 2019/1/16 13:54
 */
@Repository
public interface RoleDao extends BaseDao<RolePO> {

    /**
     * 删除角色与用户的关联
     * @param id 角色id
     * @author 阮雪峰
     * @date 2019/1/16 14:03
     */
    @Query(value = "delete from user_role where role = ?1", nativeQuery = true)
    @Modifying
    void deleteRelateUserById(Long id);

    /**
     * 删除角色权限关联
     * @param id 角色id
     * @author 阮雪峰
     * @date 2019/1/16 14:04
     */
    @Query(value = "delete from role_menu where role = ?1", nativeQuery = true)
    @Modifying
    void deleteRelateMenuById(Long id);
}
