package com.am.server.api.bulletin.dao.rdb;

import com.am.server.api.bulletin.pojo.Status;
import com.am.server.api.bulletin.pojo.ao.BulletinListAo;
import com.am.server.api.bulletin.pojo.po.BulletinDo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author 阮雪峰
 * @date 2019/1/17 15:18
 */
public interface BulletinDao {
    /**
     * 分页
     *
     * @param bulletinAo bulletinAo
     * @return Page<BulletinDo>
     */
    Page<BulletinDo> findAll(BulletinListAo bulletinAo);

    /**
     * 查询某一状态下的公告
     *
     * @param status status
     * @return List<Bulletin>
     * @author 阮雪峰
     * @date 2019/1/17 15:28
     */
    List<BulletinDo> findByStatusOrderByIdDesc(Status status);

    /**
     * save
     *
     * @param bulletinDo bulletinDo
     */
    void save(BulletinDo bulletinDo);

    /**
     * find by id
     *
     * @param id id
     * @return Optional<RoleDo>
     */
    Optional<BulletinDo> findById(Long id);

    /**
     * delete by id
     * @param id id
     */
    void deleteById(Long id);
}
