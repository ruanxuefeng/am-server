package com.am.server.api.admin.bulletin.admin.listener;

import com.am.server.api.admin.bulletin.admin.pojo.BulletinExpiredDelayedImpl;
import com.am.server.api.admin.bulletin.admin.service.BulletinService;
import com.am.server.api.admin.bulletin.admin.thread.ExpireBulletinThread;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author 阮雪峰
 * @date 2018/11/15 16:42
 */
@Slf4j
@Component
public class ServerStartedListenerForStartExpireBulletinThread implements ApplicationListener<ContextRefreshedEvent> {

    @Resource(name = "adminBulletinService")
    private BulletinService bulletinService;

    @Resource(name = "delayQueue")
    private DelayQueue<BulletinExpiredDelayedImpl> delayQueue;

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

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            POOL.execute(new ExpireBulletinThread(bulletinService, delayQueue));
        }
    }
}
