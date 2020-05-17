package com.am.server.api.role.dao.rdb;

import com.am.server.api.role.pojo.ao.RoleListAo;
import com.am.server.api.role.pojo.po.RoleDo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author 阮雪峰
 * @date 2019/1/16 13:54
 */
public interface RoleDao {
    /**
     * 分页
     *
     * @param roleListAo roleListAo
     * @return Page<RoleDo>
     */
    Page<RoleDo> findAll(RoleListAo roleListAo);

    /**
     * save
     *
     * @param role role
     */
    void save(RoleDo role);

    /**
     * find by id
     *
     * @param id id
     * @return Optional<RoleDo>
     */
    Optional<RoleDo> findById(Long id);

    /**
     * delete by id
     *
     * @param id id
     */
    void deleteById(Long id);

    /**
     * find all order by id desc
     *
     * @return List<RoleDo>
     */
    List<RoleDo> findAllOrderByIdDesc();

    /**
     * find all by ids
     * @param roleIdList roleIdList
     * @return List<RoleDo>
     */
    List<RoleDo> findAllById(List<Long> roleIdList);
}
