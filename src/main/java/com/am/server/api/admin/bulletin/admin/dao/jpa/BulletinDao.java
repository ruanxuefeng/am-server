package com.am.server.api.admin.bulletin.admin.dao.jpa;

import com.am.server.api.admin.bulletin.admin.entity.Bulletin;
import com.am.server.api.admin.bulletin.admin.entity.Status;
import com.am.server.common.base.dao.BaseDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author 阮雪峰
 * @date 2019/1/17 15:18
 */
@Repository("adminBulletinDao")
public interface BulletinDao extends BaseDao<Bulletin> {
    /**
     * 修改状态
     * @param status status
     * @param id id
     * @author 阮雪峰
     * @date 2019/1/17 15:24
     */
    @Query("update Bulletin set status = ?1 where id = ?2")
    @Modifying
    void updateStatus(Status status, Long id);

    /**
     * 查询某一状态下的公告
     * @param status status
     * @return List<Bulletin>
     * @author 阮雪峰
     * @date 2019/1/17 15:28
     */
    List<Bulletin> findByStatusOrderByIdDesc(Status status);

    /**
     * 发布公告
     * @param status status
     * @param date date
     * @param id id
     * @author 阮雪峰
     * @date 2019/1/21 15:23
     */
    @Modifying
    @Query("update Bulletin set status=:status, date=:date where id = :id")
    void publish(Status status, LocalDate date, Long id);

    /**
     * 更新主要信息
     * @param content content
     * @param days days
     * @param id id
     * @author 阮雪峰
     * @date 2019/1/21 15:39
     */
    @Modifying
    @Query("update Bulletin set content=:content, days=:days where id=:id")
    void update(String content, int days, Long id);
}
