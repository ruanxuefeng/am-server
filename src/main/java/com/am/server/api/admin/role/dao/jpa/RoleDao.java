package com.am.server.api.admin.role.dao.jpa;

import com.am.server.api.admin.role.entity.Role;
import com.am.server.common.base.dao.BaseDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author 阮雪峰
 * @date 2019/1/16 13:54
 */
@Repository("roleDao")
public interface RoleDao extends BaseDao<Role> {

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

    /**
     * 查询角色拥有的权限的主键
     * @param id role id
     * @return List<Long>
     * @author 阮雪峰
     * @date 2019/1/17 13:15
     */
    @Query(value = "select id from menu where id in (select menu from role_menu where role = ?1)", nativeQuery = true)
    List<Long> findMenuList(Long id);

    /**
     * 修改基本信息
     * @param name name
     * @param describe describe
     * @param id id
     * @author 阮雪峰
     * @date 2019/1/17 16:17
     */
    @Query(value = "update Role set name = :name, describe = :describe where id = :id")
    @Modifying
    void update(String name, String describe, Long id);
}
