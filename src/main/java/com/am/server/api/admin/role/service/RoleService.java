package com.am.server.api.admin.role.service;

import com.am.server.api.admin.role.entity.Role;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.base.service.BaseService;

import java.util.List;

/**
 *  角色
 * @author 阮雪峰
 * @date 2018/7/27 10:32
 */
public interface RoleService extends BaseService {

    /**
     * 列表
     * @param page page
     * @param role role
     * @author 阮雪峰
     * @date 2018/7/27 10:36
     */
    void list(PageVO<Role> page, Role role);

    /**
     * 新增
     * @param role role
     * @author 阮雪峰
     * @date 2018/7/27 10:36
     */
    void save(Role role);

    /**
     * 修改
     * @param role role
     * @author 阮雪峰
     * @date 2018/7/27 10:40
     */
    void update(Role role);

    /**
     * 删除
     * @param role role
     * @author 阮雪峰
     * @date 2018/7/27 10:43
     */
    void delete(Role role);

    /**
     * 查所角色
     * @return List<Role>
     * @author 阮雪峰
     * @date 2018/7/27 14:49
     */
    List<Role> findAll();

    /**
     * 修改权限
     * @param role role
     * @author 阮雪峰
     * @date 2018/7/31 8:59
     */
    void updateMenuList(Role role);

    /**
     * 查询角色拥有的权限
     * @param role role
     * @return List<Long>
     * @author 阮雪峰
     * @date 2019/2/13 16:08
     */
    List<Long> getMenuList(Role role);
}
