package com.am.server.api.bulletin.service.impl;

import com.am.server.api.bulletin.dao.rdb.BulletinDao;
import com.am.server.api.bulletin.pojo.BulletinExpiredDelayedImpl;
import com.am.server.api.bulletin.pojo.Status;
import com.am.server.api.bulletin.pojo.ao.BulletinListAo;
import com.am.server.api.bulletin.pojo.ao.SaveBulletinAo;
import com.am.server.api.bulletin.pojo.ao.UpdateBulletinAo;
import com.am.server.api.bulletin.pojo.po.BulletinDo;
import com.am.server.api.bulletin.pojo.vo.BulletinContentVo;
import com.am.server.api.bulletin.pojo.vo.BulletinListVo;
import com.am.server.api.bulletin.service.BulletinService;
import com.am.server.api.user.pojo.po.AdminUserDo;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.base.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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
@Service
public class BulletinServiceImpl implements BulletinService {

    private final BulletinDao bulletinDao;

    private final CommonService commonService;

    private final DelayQueue<BulletinExpiredDelayedImpl> delayQueue;

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
    public PageVO<BulletinListVo> list(BulletinListAo bulletinAo) {
        Page<BulletinDo> page = bulletinDao.findAll(bulletinAo);
        return new PageVO<BulletinListVo>()
                .setPage(bulletinAo.getPage())
                .setPageSize(bulletinAo.getPageSize())
                .setTotal(page.getNumber())
                .setRows(
                        page.getContent()
                                .stream().map(bulletin ->
                                new BulletinListVo().setId(bulletin.getId())
                                        .setTitle(bulletin.getTitle())
                                        .setContent(bulletin.getContent())
                                        .setCreatedTime(bulletin.getCreatedTime())
                                        .setCreatorBy(Optional.ofNullable(bulletin.getCreatedBy()).map(AdminUserDo::getUsername).orElse(""))
                                        .setDate(bulletin.getDate())
                                        .setDays(bulletin.getDays())
                                        .setStatus(bulletin.getStatus())
                        ).collect(Collectors.toList())
                );
    }

    @Commit
    @Override
    public void save(SaveBulletinAo saveBulletinAo) {
        BulletinDo bulletinDo = new BulletinDo()
                .setStatus(Status.UNPUBLISHED)
                .setTitle(saveBulletinAo.getTitle())
                .setContent(saveBulletinAo.getContent())
                .setDays(saveBulletinAo.getDays());

        commonService.beforeSave(bulletinDo);
        bulletinDao.save(bulletinDo);
    }

    @Commit
    @Override
    public void update(UpdateBulletinAo updateBulletinAo) {
        bulletinDao.findById(updateBulletinAo.getId())
                .ifPresent(bulletinDo -> {
                    bulletinDo.setTitle(updateBulletinAo.getTitle())
                            .setContent(updateBulletinAo.getContent())
                            .setDays(updateBulletinAo.getDays());
                    commonService.beforeSave(bulletinDo);
                    bulletinDao.save(bulletinDo);
                });
    }

    @Commit
    @Override
    public void publish(Long id) {
        bulletinDao.findById(id)
                .ifPresent(bulletin -> {
                    LocalDate nowDate = LocalDate.now();
                    bulletin.setStatus(Status.PUBLISHED)
                            .setDate(nowDate);
                    bulletinDao.save(bulletin);
                    delayQueue.put(new BulletinExpiredDelayedImpl(id, nowDate.atStartOfDay().plusDays(bulletin.getDays())));
                    BulletinContentVo vo = new BulletinContentVo().setTitle(bulletin.getTitle()).setContent(bulletin.getContent());
                    simpMessagingTemplate.convertAndSend("/topic/bulletin", vo);
                });
    }

    @ReadOnly
    @Override
    public List<BulletinDo> findPublishedAndUnexpiredList() {
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
    public void expire(Long id) {
        bulletinDao.findById(id).ifPresent(bulletin -> bulletinDao.save(bulletin.setStatus(Status.EXPIRED)));
    }
}
