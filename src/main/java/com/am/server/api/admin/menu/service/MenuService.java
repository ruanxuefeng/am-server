package com.am.server.api.admin.menu.service;

import com.am.server.api.admin.menu.pojo.ao.MenuListAO;
import com.am.server.api.admin.menu.pojo.ao.SaveMenuAO;
import com.am.server.api.admin.menu.pojo.ao.UpdateMenuAO;
import com.am.server.api.admin.menu.pojo.vo.MenuListVO;
import com.am.server.api.admin.menu.pojo.vo.TreeMenuVO;
import com.am.server.api.admin.role.pojo.vo.SelectRoleVO;
import com.am.server.common.base.pojo.vo.PageVO;

import java.util.List;

/**
 * @author 阮雪峰
 * @date 2018/7/30 14:59
 */
public interface MenuService {
    /**
     * 分页
     *
     * @param menuListAo menuListAo
     * @author 阮雪峰
     * @date 2018/7/30 15:36
     * @return PageVO<MenuListVO>
     */
    PageVO<MenuListVO> list(MenuListAO menuListAo);

    /**
     * 新增
     *
     * @param menu menu
     * @author 阮雪峰
     * @date 2018/7/30 15:36
     */
    void save(SaveMenuAO menu);

    /**
     * 修改
     *
     * @param menu menu
     * @author 阮雪峰
     * @date 2018/7/30 15:37
     */
    void update(UpdateMenuAO menu);

    /**
     * 删除
     *
     * @param id id
     * @author 阮雪峰
     * @date 2018/7/30 15:37
     */
    void delete(Long id);

    /**
     * 查询父级
     *
     * @return java.util.List<com.am.server.api.admin.menu.pojo.Menu>
     * @author 阮雪峰
     * @date 2018/7/30 16:08
     */
    List<SelectRoleVO> parentList();

    /**
     * 树形结构所有的菜单
     *
     * @return java.util.List<com.am.server.api.admin.menu.pojo.Menu>
     * @author 阮雪峰
     * @date 2018/7/30 16:46
     */
    List<TreeMenuVO> allMenuList();
}
