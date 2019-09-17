package com.am.server.api.bulletin.service;

import com.am.server.api.bulletin.pojo.ao.BulletinListAO;
import com.am.server.api.bulletin.pojo.ao.SaveBulletinAO;
import com.am.server.api.bulletin.pojo.ao.UpdateBulletinAO;
import com.am.server.api.bulletin.pojo.po.BulletinDO;
import com.am.server.api.bulletin.pojo.vo.BulletinListVO;
import com.am.server.common.base.pojo.vo.PageVO;

import java.util.List;

/**
 *
 * @author 阮雪峰
 * @date 2018/11/13 13:05
 */
public interface BulletinService {
    /**
     * list
     * @param bulletinAo bulletinAo
     * @return PageVO<BulletinListVO>
     * @author 阮雪峰
     * @date 2018/11/13 14:00
     */
    PageVO<BulletinListVO> list(BulletinListAO bulletinAo);

    /**
     * save
     * @param saveBulletinAo saveBulletinAo
     * @author 阮雪峰
     * @date 2018/11/13 14:00
     */
    void save(SaveBulletinAO saveBulletinAo);

    /**
     * update
     * @param updateBulletinAo updateBulletinAo
     * @author 阮雪峰
     * @date 2018/11/13 14:00
     */
    void update(UpdateBulletinAO updateBulletinAo);

    /**
     * publish
     * @param id id
     * @author 阮雪峰
     * @date 2018/11/13 14:01
     */
    void publish(Long id);

    /**
     * 查询发布并且未过期的公告
     * @return List<Bulletin>
     */
    List<BulletinDO> findPublishedAndUnexpiredList();

    /**
     * delete
     * @param id id
     * @author 阮雪峰
     * @date 2018/11/13 15:59
     */
    void delete(Long id);

    /**
     * 过期的公告
     * @param id id
     * @author 阮雪峰
     * @date 2019/1/18 12:16
     */
    void expire(Long id);
}
