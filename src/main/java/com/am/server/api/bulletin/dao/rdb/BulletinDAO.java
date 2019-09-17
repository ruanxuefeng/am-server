package com.am.server.api.bulletin.dao.rdb;

import com.am.server.api.bulletin.pojo.Status;
import com.am.server.api.bulletin.pojo.po.BulletinDO;
import com.am.server.common.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author 阮雪峰
 * @date 2019/1/17 15:18
 */
@Repository
public interface BulletinDAO extends BaseDao<BulletinDO> {

    /**
     * 查询某一状态下的公告
     * @param status status
     * @return List<Bulletin>
     * @author 阮雪峰
     * @date 2019/1/17 15:28
     */
    List<BulletinDO> findByStatusOrderByIdDesc(Status status);

}
