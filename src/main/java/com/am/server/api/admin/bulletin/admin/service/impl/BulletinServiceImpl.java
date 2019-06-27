package com.am.server.api.admin.bulletin.admin.service.impl;

import com.am.server.api.admin.bulletin.admin.dao.jpa.BulletinDao;
import com.am.server.api.admin.bulletin.admin.pojo.Bulletin;
import com.am.server.api.admin.bulletin.admin.pojo.BulletinExpiredDelayedImpl;
import com.am.server.api.admin.bulletin.admin.pojo.QBulletin;
import com.am.server.api.admin.bulletin.admin.pojo.Status;
import com.am.server.api.admin.bulletin.admin.pojo.ao.BulletinListAO;
import com.am.server.api.admin.bulletin.admin.pojo.ao.SaveBulletinAO;
import com.am.server.api.admin.bulletin.admin.pojo.ao.UpdateBulletinAO;
import com.am.server.api.admin.bulletin.admin.pojo.po.BulletinPO;
import com.am.server.api.admin.bulletin.admin.pojo.vo.BulletinContentVO;
import com.am.server.api.admin.bulletin.admin.pojo.vo.BulletinListVO;
import com.am.server.api.admin.bulletin.admin.service.BulletinService;
import com.am.server.api.admin.user.pojo.po.QAdminUserPO;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.base.service.CommonService;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.IdUtils;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.DelayQueue;
import java.util.stream.Collectors;

/**
 * @author 阮雪峰
 * @date 2018/11/13 13:05
 */
@Service("adminBulletinService")
public class BulletinServiceImpl implements BulletinService {

    private final BulletinDao bulletinDao;

    private final CommonService commonService;

    private final DelayQueue<BulletinExpiredDelayedImpl> delayQueue;

    @PersistenceContext
    private EntityManager entityManager;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public BulletinServiceImpl(BulletinDao bulletinDao, CommonService commonService, DelayQueue<BulletinExpiredDelayedImpl> delayQueue, SimpMessagingTemplate simpMessagingTemplate) {
        this.bulletinDao = bulletinDao;
        this.commonService = commonService;
        this.delayQueue = delayQueue;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @ReadOnly
    @Override
    public PageVO<BulletinListVO> list(BulletinListAO bulletinAo) {
        PageVO<BulletinListVO> page = new PageVO<BulletinListVO>().setPage(bulletinAo.getPage()).setPageSize(bulletinAo.getPageSize());
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QBulletin qBulletin = QBulletin.bulletin;
        QAdminUserPO qAdminUser = QAdminUserPO.adminUserPO;

        JPAQuery<Tuple> query = jpaQueryFactory.select(
                qBulletin.id,
                qBulletin.content,
                qBulletin.createTime,
                qBulletin.status,
                qBulletin.days,
                qBulletin.date,
                qAdminUser.name.as(Constant.CREATOR_NAME)
        )
                .from(qBulletin)
                .leftJoin(qAdminUser).on(qBulletin.creator.eq(qAdminUser.id))
                .offset(page.getCol())
                .limit(page.getPageSize())
                .orderBy(qBulletin.id.desc());

        Optional.of(bulletinAo)
                .map(BulletinListAO::getContent)
                .ifPresent(content -> query.where(qBulletin.content.like("%" + content + "%")));

        List<BulletinListVO> list = query.fetch()
                .stream().map(
                        tuple -> new BulletinListVO()
                                .setId(tuple.get(qBulletin.id))
                                .setContent(tuple.get(qBulletin.content))
                                .setCreateTime(tuple.get(qBulletin.createTime))
                                .setStatus(tuple.get(qBulletin.status))
                                .setDays(tuple.get(qBulletin.days))
                                .setDate(tuple.get(qBulletin.date))
                                .setCreatorName(tuple.get(qAdminUser.name.as(Constant.CREATOR_NAME)))
                ).collect(Collectors.toList());
        page.setTotal((int) query.fetchCount());
        page.setRows(list);

        return page;
    }

    @Commit
    @Override
    public void save(SaveBulletinAO saveBulletinAo) {
        bulletinDao.save(new BulletinPO()
                .setId(IdUtils.getId())
                .setStatus(Status.UNPUBLISHED)
                .setCreateTime(LocalDateTime.now())
                .setCreator(commonService.getLoginUserId())
                .setContent(saveBulletinAo.getContent())
                .setDays(saveBulletinAo.getDays()));
    }

    @Commit
    @Override
    public void update(UpdateBulletinAO updateBulletinAo) {
        bulletinDao.save(new BulletinPO()
                .setId(updateBulletinAo.getId())
                .setContent(updateBulletinAo.getContent())
                .setDays(updateBulletinAo.getDays()));
    }

    @Commit
    @Override
    public void publish(Long id) {
        bulletinDao.findById(id)
                .ifPresent(bulletin -> {
                    LocalDate nowDate = LocalDate.now();
                    bulletinDao.publish(Status.PUBLISHED, nowDate, id);
                    delayQueue.put(new BulletinExpiredDelayedImpl(id, nowDate.atStartOfDay().plusDays(bulletin.getDays())));
                    simpMessagingTemplate.convertAndSend("/topic/notice", new BulletinContentVO(bulletin.getContent()));
                });
    }

    @ReadOnly
    @Override
    public List<BulletinPO> findPublishedAndUnexpiredList() {
        return bulletinDao.findByStatusOrderByIdDesc(Status.PUBLISHED);
    }

    @Commit
    @Override
    public void delete(Long id) {
        bulletinDao.deleteById(id);
        delayQueue.remove(new BulletinExpiredDelayedImpl(id, LocalDateTime.now()));
    }

    @Commit
    @Override
    public void expire(Bulletin bulletin) {
        bulletinDao.updateStatus(Status.EXPIRED, bulletin.getId());
    }
}
