package com.am.server.common.util;

/**
 * 获取唯一ID 的工具类
 *
 * @author bowen_wang
 */
public class IdUtils {

    public static Long getId() {
        return getInstance().nextId();
    }

    private static IdWorker getInstance() {
        return IdWorker.INSTANCE;
    }

    /**
     * 全局唯一的ID 工厂 使用的是 Twitter-Snowflake 算法 可以参考
     * -----http://yuanhsh.iteye.com/blog/2209696 经过测试 在4个线程 同事不间断生产 1000W主键的时候
     * 性能和准确性都能得到保障 每秒能产生26W+的唯一主键
     *
     * @author wangbo
     */
    private static class IdWorker {

        private final static IdWorker INSTANCE = new IdWorker(1L);

        private final static long TWEPOCH = 1361753741828L;
        private final static long WORKER_ID_BITS = 10L;
        /**
         * 这个是二进制运算，就是 5 bit最多只能有31个数字，也就是说机器id最多只能是32以内
         */
        private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
        private final static long SEQUENCE_BITS = 12L;
        private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
        private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
        private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
        private final long workerId;
        private long sequence = 0L;
        private long lastTimestamp = -1L;

        IdWorker(long workerId) {
            super();
            if (workerId > MAX_WORKER_ID || workerId < 0) {
                throw new IllegalArgumentException(String.format(
                        "worker Id can't be greater than %d or less than 0",
                        MAX_WORKER_ID));
            }
            this.workerId = workerId;
        }

        synchronized long nextId() {
            long timestamp = timeGen();
            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & SEQUENCE_MASK;
                if (sequence == 0) {
                    timestamp = tilNextMillis(lastTimestamp);
                }
            } else {
                sequence = 0;
            }
            if (timestamp < lastTimestamp) {
                try {
                    throw new Exception(
                            String.format(
                                    "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                                    lastTimestamp - timestamp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            lastTimestamp = timestamp;
            return ((timestamp - TWEPOCH << TIMESTAMP_LEFT_SHIFT))
                    | (workerId << WORKER_ID_SHIFT) | (sequence);
        }

        private long tilNextMillis(long lastTimestamp) {
            long timestamp = timeGen();
            while (timestamp <= lastTimestamp) {
                timestamp = timeGen();
            }
            return timestamp;
        }

        private long timeGen() {
            return System.currentTimeMillis();
        }
    }
}
