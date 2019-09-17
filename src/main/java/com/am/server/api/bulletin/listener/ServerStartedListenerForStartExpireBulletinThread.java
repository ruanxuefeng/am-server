package com.am.server.api.bulletin.listener;

import com.am.server.api.bulletin.pojo.BulletinExpiredDelayedImpl;
import com.am.server.api.bulletin.service.BulletinService;
import com.am.server.api.bulletin.thread.ExpireBulletinThread;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 *
 * @author 阮雪峰
 * @date 2018/11/15 16:42
 */
@Slf4j
@Component
public class ServerStartedListenerForStartExpireBulletinThread implements ApplicationListener<ContextRefreshedEvent> {

    private final BulletinService bulletinService;

    private final DelayQueue<BulletinExpiredDelayedImpl> delayQueue;

    private static final ThreadFactory EXPIRE_BULLETIN_THREAD_FACTORY = new ThreadFactoryBuilder()
            .setNameFormat("expire-bulletin-pool-%d").build();

    private static final ExecutorService POOL = new ThreadPoolExecutor(
            1,
            1,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024),
            EXPIRE_BULLETIN_THREAD_FACTORY,
            new ThreadPoolExecutor.AbortPolicy()
    );

    public ServerStartedListenerForStartExpireBulletinThread(BulletinService bulletinService, DelayQueue<BulletinExpiredDelayedImpl> delayQueue) {
        this.bulletinService = bulletinService;
        this.delayQueue = delayQueue;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            POOL.execute(new ExpireBulletinThread(bulletinService, delayQueue));
        }
    }
}
