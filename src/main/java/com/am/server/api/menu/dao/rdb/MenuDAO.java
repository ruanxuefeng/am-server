package com.am.server.api.menu.dao.rdb;

import com.am.server.api.menu.pojo.po.MenuDO;
import com.am.server.common.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author 阮雪峰
 * @date 2019/1/15 14:17
 */
@Repository
public interface MenuDAO extends BaseDao<MenuDO> {

    /**
     * 查询某一层级的权限
     * @param level level
     * @return List<Menu>
     * @author 阮雪峰
     * @date 2019/2/14 9:11
     */
    List<MenuDO> findAllByLevelOrderByIdDesc(int level);
}
