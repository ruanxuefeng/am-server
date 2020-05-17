package com.am.server.api.bulletin.repository;

import com.am.server.api.bulletin.pojo.Status;
import com.am.server.api.bulletin.pojo.po.BulletinDo;
import com.am.server.common.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 阮雪峰
 * @date 2020年5月17日18:30:08
 */
@Repository
public interface BulletinRepository extends BaseRepository<BulletinDo> {
    /**
     * 查询某一状态下的公告
     * @param status status
     * @return List<Bulletin>
     * @author 阮雪峰
     * @date 2019/1/17 15:28
     */
    List<BulletinDo> findByStatusOrderByIdDesc(Status status);
}
