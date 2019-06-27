package com.am.server.api.admin.bulletin.admin.thread;

import com.am.server.api.admin.bulletin.admin.pojo.Bulletin;
import com.am.server.api.admin.bulletin.admin.pojo.BulletinExpiredDelayedImpl;
import com.am.server.api.admin.bulletin.admin.service.BulletinService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.DelayQueue;

/**
 *
 * @author 阮雪峰
 * @date 2018/11/15 16:52
 */
@Slf4j
public class ExpireBulletinThread extends Thread {

    private final BulletinService bulletinService;

    private final DelayQueue<BulletinExpiredDelayedImpl> delayQueue;

    public ExpireBulletinThread(BulletinService bulletinService, DelayQueue<BulletinExpiredDelayedImpl> delayQueue) {
        this.bulletinService = bulletinService;
        this.delayQueue = delayQueue;
    }

    @Override
    public void run() {
        BulletinExpiredDelayedImpl delayed = null;
        while (true) {
            try {

                delayed = delayQueue.take();
                bulletinService.expire(new Bulletin(delayed.getId()));
                log.info("------ id为" + delayed.getId() + "公告过期 ------");

            } catch (InterruptedException e) {
                if (delayed != null) {
                    log.error("------ id为" + delayed.getId() + "过期失败 ------", e);
                }
            }
        }
    }


}
