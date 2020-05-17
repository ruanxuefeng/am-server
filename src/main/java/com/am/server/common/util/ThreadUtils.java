package com.am.server.common.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 线程相关工具类
 *
 * @author 阮雪峰
 * @date 2020年4月30日20:15:47
 */
public class ThreadUtils {
    private ThreadUtils() {
    }

    private static final ThreadFactory EXPIRE_BULLETIN_THREAD_FACTORY = new ThreadFactoryBuilder().build();

    private static final ExecutorService POOL = new ThreadPoolExecutor(
            16,
            16,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(16),
            EXPIRE_BULLETIN_THREAD_FACTORY,
            new ThreadPoolExecutor.AbortPolicy()
    );

    public static void execute(Runnable runnable) {
        POOL.execute(runnable);
    }
}
