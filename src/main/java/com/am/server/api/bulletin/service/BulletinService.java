package com.am.server.api.bulletin.service;

import com.am.server.api.bulletin.pojo.ao.BulletinListAo;
import com.am.server.api.bulletin.pojo.ao.SaveBulletinAo;
import com.am.server.api.bulletin.pojo.ao.UpdateBulletinAo;
import com.am.server.api.bulletin.pojo.po.BulletinDo;
import com.am.server.api.bulletin.pojo.vo.BulletinListVo;
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
    PageVO<BulletinListVo> list(BulletinListAo bulletinAo);

    /**
     * save
     * @param saveBulletinAo saveBulletinAo
     * @author 阮雪峰
     * @date 2018/11/13 14:00
     */
    void save(SaveBulletinAo saveBulletinAo);

    /**
     * update
     * @param updateBulletinAo updateBulletinAo
     * @author 阮雪峰
     * @date 2018/11/13 14:00
     */
    void update(UpdateBulletinAo updateBulletinAo);

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
    List<BulletinDo> findPublishedAndUnexpiredList();

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
