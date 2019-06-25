package com.am.server.api.admin.menu.dao.jpa;

import com.am.server.api.admin.menu.pojo.po.MenuPO;
import com.am.server.common.base.dao.BaseDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author 阮雪峰
 * @date 2019/1/15 14:17
 */
@Repository
public interface MenuDao extends BaseDao<MenuPO> {
    /**
     * 删除菜单和角色关联
     * @param menu 菜单id
     * @author 阮雪峰
     * @date 2019/1/16 11:39
     */
    @Query(value = "delete from role_menu where menu = ?1", nativeQuery = true)
    @Modifying
    void deleteRelateRoles(long menu);

    /**
     * 查询某一层级的权限
     * @param level level
     * @return List<Menu>
     * @author 阮雪峰
     * @date 2019/2/14 9:11
     */
    List<MenuPO> findAllByLevelOrderByIdDesc(int level);
}
