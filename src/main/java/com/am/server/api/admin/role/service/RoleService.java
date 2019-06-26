package com.am.server.api.admin.role.service;

import com.am.server.api.admin.role.pojo.ao.RoleListAO;
import com.am.server.api.admin.role.pojo.ao.SaveRoleAO;
import com.am.server.api.admin.role.pojo.ao.UpdateRoleMenuAO;
import com.am.server.api.admin.role.pojo.ao.UpdateRoleAO;
import com.am.server.api.admin.role.pojo.vo.RoleListVo;
import com.am.server.api.admin.role.pojo.vo.SelectRoleVO;
import com.am.server.common.base.pojo.vo.PageVO;

import java.util.List;

/**
 *  角色
 * @author 阮雪峰
 * @date 2018/7/27 10:32
 */
public interface RoleService {

    /**
     * 列表
     * @param roleListAo roleListAo
     * @return PageVO<RoleListVo>
     * @author 阮雪峰
     * @date 2018/7/27 10:36
     */
    PageVO<RoleListVo> list(RoleListAO roleListAo);

    /**
     * 新增
     * @param role role
     * @author 阮雪峰
     * @date 2018/7/27 10:36
     */
    void save(SaveRoleAO role);

    /**
     * 修改
     * @param roleAo roleAo
     * @author 阮雪峰
     * @date 2018/7/27 10:40
     */
    void update(UpdateRoleAO roleAo);

    /**
     * 删除
     * @param id id
     * @author 阮雪峰
     * @date 2018/7/27 10:43
     */
    void delete(Long id);

    /**
     * 查所角色
     * @return List<RoleListVo>
     * @author 阮雪峰
     * @date 2018/7/27 14:49
     */
    List<SelectRoleVO> findAll();

    /**
     * 修改权限
     * @param updateRoleMenuAo updateRoleMenuAo
     * @author 阮雪峰
     * @date 2018/7/31 8:59
     */
    void updateMenuList(UpdateRoleMenuAO updateRoleMenuAo);

    /**
     * 查询角色拥有的权限
     * @param id id
     * @return List<Long>
     * @author 阮雪峰
     * @date 2019/2/13 16:08
     */
    List<Long> getMenuList(Long id);
}
