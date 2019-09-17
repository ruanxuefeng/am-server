package com.am.server.api.bulletin.service.impl;

import com.am.server.api.bulletin.dao.rdb.BulletinDAO;
import com.am.server.api.bulletin.pojo.BulletinExpiredDelayedImpl;
import com.am.server.api.bulletin.pojo.Status;
import com.am.server.api.bulletin.pojo.ao.BulletinListAO;
import com.am.server.api.bulletin.pojo.ao.SaveBulletinAO;
import com.am.server.api.bulletin.pojo.ao.UpdateBulletinAO;
import com.am.server.api.bulletin.pojo.po.BulletinDO;
import com.am.server.api.bulletin.pojo.vo.BulletinContentVO;
import com.am.server.api.bulletin.pojo.vo.BulletinListVO;
import com.am.server.api.bulletin.service.BulletinService;
import com.am.server.api.user.pojo.po.AdminUserDO;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.base.service.CommonService;
import com.am.server.common.util.IdUtils;
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

    private final BulletinDAO bulletinDAO;

    private final CommonService commonService;

    private final DelayQueue<BulletinExpiredDelayedImpl> delayQueue;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public BulletinServiceImpl(BulletinDAO bulletinDAO, CommonService commonService, DelayQueue<BulletinExpiredDelayedImpl> delayQueue, SimpMessagingTemplate simpMessagingTemplate) {
        this.bulletinDAO = bulletinDAO;
        this.commonService = commonService;
        this.delayQueue = delayQueue;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @ReadOnly
    @Override
    public PageVO<BulletinListVO> list(BulletinListAO bulletinAo) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                .withMatcher("content", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<BulletinDO> example = Example.of(new BulletinDO().setContent(bulletinAo.getContent()), matcher);

        Page<BulletinDO> page = bulletinDAO.findAll(example, PageRequest.of(bulletinAo.getPage() - 1, bulletinAo.getPageSize()));
        return new PageVO<BulletinListVO>()
                .setPage(bulletinAo.getPage())
                .setPageSize(bulletinAo.getPageSize())
                .setTotal(page.getNumber())
                .setRows(
                        page.getContent()
                                .stream().map(bulletin ->
                                new BulletinListVO().setId(bulletin.getId())
                                        .setContent(bulletin.getContent())
                                .setCreateTime(bulletin.getCreatedTime())
                                .setCreatorBy(Optional.ofNullable(bulletin.getCreatedBy()).map(AdminUserDO::getUsername).orElse(""))
                                .setDate(bulletin.getDate())
                                .setDays(bulletin.getDays())
                                .setStatus(bulletin.getStatus())
                        ).collect(Collectors.toList())
                );
    }

    @Commit
    @Override
    public void save(SaveBulletinAO saveBulletinAo) {
        bulletinDAO.save(new BulletinDO()
                .setId(IdUtils.getId())
                .setStatus(Status.UNPUBLISHED)
                .setCreatedTime(LocalDateTime.now())
                .setCreatedBy(new AdminUserDO().setId(commonService.getLoginUserId()))
                .setContent(saveBulletinAo.getContent())
                .setDays(saveBulletinAo.getDays()));
    }

    @Commit
    @Override
    public void update(UpdateBulletinAO updateBulletinAo) {
        bulletinDAO.save(new BulletinDO()
                .setId(updateBulletinAo.getId())
                .setContent(updateBulletinAo.getContent())
                .setDays(updateBulletinAo.getDays()));
    }

    @Commit
    @Override
    public void publish(Long id) {
        bulletinDAO.findById(id)
                .ifPresent(bulletin -> {
                    LocalDate nowDate = LocalDate.now();
                    bulletinDAO.save(bulletin.setStatus(Status.PUBLISHED).setDate(nowDate));
                    delayQueue.put(new BulletinExpiredDelayedImpl(id, nowDate.atStartOfDay().plusDays(bulletin.getDays())));
                    simpMessagingTemplate.convertAndSend("/topic/notice", new BulletinContentVO(bulletin.getContent()));
                });
    }

    @ReadOnly
    @Override
    public List<BulletinDO> findPublishedAndUnexpiredList() {
        return bulletinDAO.findByStatusOrderByIdDesc(Status.PUBLISHED);
    }

    @Commit
    @Override
    public void delete(Long id) {
        bulletinDAO.deleteById(id);
        delayQueue.remove(new BulletinExpiredDelayedImpl(id, LocalDateTime.now()));
    }

    @Commit
    @Override
    public void expire(Long id) {
        bulletinDAO.findById(id).ifPresent(bulletin -> bulletinDAO.save(bulletin.setStatus(Status.EXPIRED)));
    }
}
