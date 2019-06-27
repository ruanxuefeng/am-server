package com.am.server.api.admin.bulletin.admin.service;

import com.am.server.api.admin.bulletin.admin.pojo.Bulletin;
import com.am.server.api.admin.bulletin.admin.pojo.ao.BulletinListAO;
import com.am.server.api.admin.bulletin.admin.pojo.ao.SaveBulletinAO;
import com.am.server.api.admin.bulletin.admin.pojo.ao.UpdateBulletinAO;
import com.am.server.api.admin.bulletin.admin.pojo.po.BulletinPO;
import com.am.server.api.admin.bulletin.admin.pojo.vo.BulletinListVO;
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
    List<BulletinPO> findPublishedAndUnexpiredList();

    /**
     * delete
     * @param id id
     * @author 阮雪峰
     * @date 2018/11/13 15:59
     */
    void delete(Long id);

    /**
     * 过期的公告
     * @param bulletin bulletin
     * @author 阮雪峰
     * @date 2019/1/18 12:16
     */
    void expire(Bulletin bulletin);
}
