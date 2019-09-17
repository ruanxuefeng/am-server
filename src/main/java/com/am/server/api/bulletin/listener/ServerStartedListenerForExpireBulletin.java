package com.am.server.api.bulletin.listener;

import com.am.server.api.bulletin.pojo.BulletinExpiredDelayedImpl;
import com.am.server.api.bulletin.pojo.po.BulletinDO;
import com.am.server.api.bulletin.service.BulletinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.DelayQueue;

/**
 * 服务启动完成后查找在服务停止期间应该过期的公告
 *
 * @author 阮雪峰
 * @date 2018/11/1 12:24
 */
@Slf4j
@Component
public class ServerStartedListenerForExpireBulletin implements ApplicationListener<ContextRefreshedEvent> {

    private final BulletinService bulletinService;

    private final DelayQueue<BulletinExpiredDelayedImpl> delayQueue;

    public ServerStartedListenerForExpireBulletin(BulletinService bulletinService, DelayQueue<BulletinExpiredDelayedImpl> delayQueue) {
        this.bulletinService = bulletinService;
        this.delayQueue = delayQueue;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            List<BulletinDO> list = bulletinService.findPublishedAndUnexpiredList();
            list.forEach(bulletin -> {
                try {
                    BulletinExpiredDelayedImpl delayed = new BulletinExpiredDelayedImpl(bulletin.getId(), bulletin.getDate().plusDays(bulletin.getDays()).atTime(LocalTime.now()));
                    if (!delayQueue.contains(delayed)) {
                        delayQueue.put(delayed);
                    }
                } catch (Exception e) {
                    log.error("------ id为[{}]公告已放入对列失败 ------", bulletin.getId(), e);
                }
            });
        }
    }

}
