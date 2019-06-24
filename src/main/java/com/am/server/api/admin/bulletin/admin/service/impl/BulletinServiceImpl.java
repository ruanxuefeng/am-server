package com.am.server.api.admin.bulletin.admin.service.impl;

import com.am.server.advice.update.annotation.Save;
import com.am.server.api.admin.bulletin.admin.dao.jpa.BulletinDao;
import com.am.server.api.admin.bulletin.admin.entity.Bulletin;
import com.am.server.api.admin.bulletin.admin.entity.BulletinExpiredDelayedImpl;
import com.am.server.api.admin.bulletin.admin.entity.QBulletin;
import com.am.server.api.admin.bulletin.admin.entity.Status;
import com.am.server.api.admin.bulletin.admin.service.BulletinService;
import com.am.server.api.admin.user.pojo.po.QAdminUserPO;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.constant.Constant;
import com.querydsl.core.types.Projections;
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

/**
 * @author 阮雪峰
 * @date 2018/11/13 13:05
 */
@Service("adminBulletinService")
public class BulletinServiceImpl implements BulletinService {

    private final BulletinDao bulletinDao;


    private final DelayQueue<BulletinExpiredDelayedImpl> delayQueue;

    @PersistenceContext
    private EntityManager entityManager;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public BulletinServiceImpl(BulletinDao bulletinDao, DelayQueue<BulletinExpiredDelayedImpl> delayQueue, SimpMessagingTemplate simpMessagingTemplate) {
        this.bulletinDao = bulletinDao;
        this.delayQueue = delayQueue;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @ReadOnly
    @Override
    public void list(PageVO<Bulletin> page, Bulletin bulletin) {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QBulletin qBulletin = QBulletin.bulletin;
        QAdminUserPO qAdminUser = QAdminUserPO.adminUserPO;

        JPAQuery<Bulletin> query = jpaQueryFactory.select(
                Projections.bean(
                        Bulletin.class,
                        qBulletin.id,
                        qBulletin.content,
                        qBulletin.createTime,
                        qBulletin.status,
                        qBulletin.days,
                        qBulletin.date,
                        qAdminUser.name.as(Constant.CREATOR_NAME)
                )
        )
                .from(qBulletin)
                .leftJoin(qAdminUser).on(qBulletin.creator.eq(qAdminUser.id))
                .offset(page.getCol())
                .limit(page.getPageSize())
                .orderBy(qBulletin.id.desc());

        Optional.of(bulletin)
                .map(Bulletin::getContent)
                .ifPresent(content -> query.where(qBulletin.content.like("%" + content + "%")));

        page.setTotal((int) query.fetchCount());
        page.setRows(query.fetch());
    }

    @Commit
    @Save
    @Override
    public void save(Bulletin bulletin) {
        bulletin.setStatus(Status.UNPUBLISHED);
        bulletinDao.save(bulletin);
    }

    @Commit
    @Override
    public void update(Bulletin bulletin) {
        bulletinDao.save(bulletin);
    }

    @Commit
    @Override
    public void publish(Bulletin bulletin) {

        bulletinDao.findById(bulletin.getId())
                .ifPresent(item -> {
                    LocalDate nowDate = LocalDate.now();
                    bulletinDao.publish(Status.PUBLISHED, nowDate, item.getId());
                    delayQueue.put(new BulletinExpiredDelayedImpl(item.getId(), nowDate.atStartOfDay().plusDays(item.getDays())));
                    simpMessagingTemplate.convertAndSend("/topic/notice", item);
                });
    }

    @ReadOnly
    @Override
    public List<Bulletin> findPublishedAndUnexpiredList() {
        return bulletinDao.findByStatusOrderByIdDesc(Status.PUBLISHED);
    }

    @Commit
    @Override
    public void delete(Bulletin bulletin) {
        bulletinDao.delete(bulletin);
        delayQueue.remove(new BulletinExpiredDelayedImpl(bulletin.getId(), LocalDateTime.now()));
    }

    @Commit
    @Override
    public void expire(Bulletin bulletin) {
        bulletinDao.updateStatus(Status.EXPIRED, bulletin.getId());
    }
}
