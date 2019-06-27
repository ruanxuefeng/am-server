package com.am.server.api.admin.bulletin.admin.pojo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author 阮雪峰
 * @date 2018/11/15 16:28
 */
@Data
public class BulletinExpiredDelayedImpl implements Delayed {

    public BulletinExpiredDelayedImpl(Long id, LocalDateTime executeTime) {
        this.id = id;
        this.executeTime = executeTime;
    }

    private Long id;
    private LocalDateTime executeTime;

    @Override
    public long getDelay(TimeUnit unit) {
        return executeTime.isBefore(LocalDateTime.now()) ? -1 : 1;
    }

    @Override
    public int compareTo(Delayed o) {
        if (this == o) {
            return 0;
        }
        if (o instanceof BulletinExpiredDelayedImpl) {
            return this.executeTime.compareTo(((BulletinExpiredDelayedImpl) o).getExecuteTime());
        } else {
            throw new RuntimeException("不是com.am.server.api.admin.bulletin.admin.pojo.BulletinExpiredDelayedImpl");
        }
    }
}
