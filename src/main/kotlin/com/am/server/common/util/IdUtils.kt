package com.am.server.common.util

object IdUtils{

    @JvmStatic fun getId(): Long {
        return getInstance().nextId()
    }

    private fun getInstance(): IdWorker {
        return IdWorker.INSTANCE
    }

    /**
     * 全局唯一的ID 工厂 使用的是 Twitter-Snowflake 算法 可以参考
     * -----http://yuanhsh.iteye.com/blog/2209696 经过测试 在4个线程 同事不间断生产 1000W主键的时候
     * 性能和准确性都能得到保障 每秒能产生26W+的唯一主键
     *
     * @author wangbo
     */
    private class IdWorker internal constructor(private val workerId: Long) {
        private var sequence = 0L
        private var lastTimestamp = -1L

        init {
            if (workerId > MAX_WORKER_ID || workerId < 0) {
                throw IllegalArgumentException(String.format(
                        "worker Id can't be greater than %d or less than 0",
                        MAX_WORKER_ID))
            }
        }

        @Synchronized
        internal fun nextId(): Long {
            var timestamp = timeGen()
            if (lastTimestamp == timestamp) {
                sequence = sequence + 1 and SEQUENCE_MASK
                if (sequence == 0L) {
                    timestamp = tilNextMillis(lastTimestamp)
                }
            } else {
                sequence = 0
            }
            if (timestamp < lastTimestamp) {
                try {
                    throw Exception(
                            String.format(
                                    "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                                    lastTimestamp - timestamp))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            lastTimestamp = timestamp
            return (timestamp - TWEPOCH shl TIMESTAMP_LEFT_SHIFT
                    or (workerId shl WORKER_ID_SHIFT) or sequence)
        }

        private fun tilNextMillis(lastTimestamp: Long): Long {
            var timestamp = timeGen()
            while (timestamp <= lastTimestamp) {
                timestamp = timeGen()
            }
            return timestamp
        }

        private fun timeGen(): Long {
            return System.currentTimeMillis()
        }

        companion object {
            @JvmStatic val INSTANCE = IdWorker(1L)

            private const val TWEPOCH = 1361753741828L
            private const val WORKER_ID_BITS = 10
            /**
             * 这个是二进制运算，就是 5 bit最多只能有31个数字，也就是说机器id最多只能是32以内
             */
            private const val MAX_WORKER_ID = (-1L shl WORKER_ID_BITS).inv()
            private const val SEQUENCE_BITS = 12
            private const val SEQUENCE_MASK = (-1L shl SEQUENCE_BITS).inv()
            private const val WORKER_ID_SHIFT = SEQUENCE_BITS
            private const val TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS
        }
    }
}
