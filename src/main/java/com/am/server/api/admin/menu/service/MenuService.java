package com.am.server.api.admin.menu.service;

import com.am.server.api.admin.menu.entity.Menu;
import com.am.server.common.base.pojo.vo.PageVO;

import java.util.List;

/**
 *
 * @author 阮雪峰
 * @date 2018/7/30 14:59
 */
public interface MenuService {
    /**
     * 分页
     * @param page page
     * @param menu menu
     * @author 阮雪峰
     * @date 2018/7/30 15:36
     */
    void list(PageVO<Menu> page, Menu menu);

    /**
     * 新增
     * @param menu menu
     * @author 阮雪峰
     * @date 2018/7/30 15:36
     */
    void save(Menu menu);

    /**
     * 详情
     * @param menu menu
     * @return com.am.server.api.admin.menu.pojo.Menu
     * @author 阮雪峰
     * @date 2018/7/30 15:37
     */
    Menu detail(Menu menu);

    /**
     * 修改
     * @param menu menu
     * @author 阮雪峰
     * @date 2018/7/30 15:37
     */
    void update(Menu menu);

    /**
     * 删除
     * @param menu menu
     * @author 阮雪峰
     * @date 2018/7/30 15:37
     */
    void delete(Menu menu);

    /**
     * 查询父级
     * @return java.util.List<com.am.server.api.admin.menu.pojo.Menu>
     * @author 阮雪峰
     * @date 2018/7/30 16:08
     */
    List<Menu> parentList();

    /**
     * 树形结构所有的菜单
     * @return java.util.List<com.am.server.api.admin.menu.pojo.Menu>
     * @author 阮雪峰
     * @date 2018/7/30 16:46
     */
    List<Menu> menuList();
}
