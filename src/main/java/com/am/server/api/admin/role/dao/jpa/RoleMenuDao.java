package com.am.server.api.admin.role.dao.jpa;

import com.am.server.api.admin.role.pojo.po.RoleMenuPO;
import com.am.server.common.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author 阮雪峰
 * @date 2019/2/13 15:33
 */
@Repository
public interface RoleMenuDao extends BaseDao<RoleMenuPO> {
    /**
     * 删除角色权限管理关系
     * @param role 角色id
     * @author 阮雪峰
     * @date 2019/2/13 15:35
     */
    void deleteByRole(Long role);
}
