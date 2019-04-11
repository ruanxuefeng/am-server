package com.am.server.api.admin.bulletin.admin.service;

import com.am.server.api.admin.bulletin.admin.entity.Bulletin;
import com.am.server.common.base.page.Page;

import java.util.List;

/**
 *
 * @author 阮雪峰
 * @date 2018/11/13 13:05
 */
public interface BulletinService {
    /**
     * list
     * @param page page
     * @param bulletin bulletin
     * @author 阮雪峰
     * @date 2018/11/13 14:00
     */
    void list(Page<Bulletin> page, Bulletin bulletin);

    /**
     * save
     * @param bulletin bulletin
     * @author 阮雪峰
     * @date 2018/11/13 14:00
     */
    void save(Bulletin bulletin);

    /**
     * update
     * @param bulletin bulletin
     * @author 阮雪峰
     * @date 2018/11/13 14:00
     */
    void update(Bulletin bulletin);

    /**
     * publish
     * @param bulletin bulletin
     * @author 阮雪峰
     * @date 2018/11/13 14:01
     */
    void publish(Bulletin bulletin);

    /**
     * 查询发布并且未过期的公告
     * @param bulletin bulletin
     * @return List<Bulletin>
     */
    List<Bulletin> findPublishedAndUnexpiredList();

    /**
     * delete
     * @param bulletin bulletin
     * @author 阮雪峰
     * @date 2018/11/13 15:59
     */
    void delete(Bulletin bulletin);

    /**
     * 过期的公告
     * @param bulletin bulletin
     * @author 阮雪峰
     * @date 2019/1/18 12:16
     */
    void expire(Bulletin bulletin);
}
