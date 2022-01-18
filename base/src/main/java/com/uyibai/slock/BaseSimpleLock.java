package com.uyibai.slock;

import com.uyibai.slock.exception.SimpleLockException;
import com.uyibai.slock.utils.StringUtils;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract Simple Lock
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date : 2022/1/18
 */
public abstract class BaseSimpleLock implements SimpleLock {
    /**
     * 锁 key
     */
    protected String key;
    /**
     * 锁值
     */
    protected String value;

    /**
     * 过期时间，单位s
     */
    protected Integer ttl;
    /**
     * 时间单位
     */
    protected TimeUnit timeUnit;

    /**
     * 是否支持锁重入
     */
    protected boolean reentrant;

    /**
     * 默认过期时间，单位s
     */
    protected final static int DEFAULT_TTL = 10;
    /**
     * 默认过期时间单位
     */
    protected final static TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * 活跃重入锁
     */
    protected static final ThreadLocal<HashSet<String>> holdLocks = new ThreadLocal<HashSet<String>>() {
        @Override
        protected HashSet<String> initialValue() {
            return new HashSet<String>();
        }
    };

    /**
     * 是否已经持有锁
     */
    protected final AtomicBoolean hold = new AtomicBoolean(false);

    public BaseSimpleLock(String key, String value, TimeUnit timeUnit, boolean reentrant) {
        this.key = key;
        this.value = value;
        this.ttl = DEFAULT_TTL;
        this.timeUnit = timeUnit != null ? timeUnit : DEFAULT_TIME_UNIT;
        this.reentrant = reentrant;
    }

    /**
     * 获取锁，如果没得到，不阻塞
     *
     * @param ttl 过期时间，ms
     * @return 成功 返回 true, 否则返回 false
     * @throws SimpleLockException 锁异常
     */
    @Override
    public boolean acquire(int ttl) throws SimpleLockException {
        if (getClient() == null || !StringUtils.hasLength(key)) {
            // 空锁
            return false;
        } else if (reentrant && holdLocks.get().contains(key)) {
            // 重入锁
            return true;
        }
        this.ttl = (ttl <= 0 ? DEFAULT_TTL : ttl);
        return lock();
    }

    /**
     * 获取锁，直到超时
     *
     * @param ttl      过期时间，单位s
     * @param interval 重试间隔时间，单位s
     * @param maxRetry 最大重试次数
     * @return 成功 返回 true, 否则返回 false
     * @throws SimpleLockException 锁异常
     */
    @Override
    public boolean acquire(int ttl, long interval, int maxRetry) throws SimpleLockException {
        if (getClient() == null || !StringUtils.hasLength(key)) {
            // 空锁
            return false;
        } else if (reentrant && holdLocks.get().contains(key)) {
            // 重入锁
            return true;
        }
        this.ttl = (ttl <= 0 ? DEFAULT_TTL : ttl);
        try {
            if (!lock()) {
                // 重试抢锁
                if (maxRetry > 0) {
                    Thread.sleep((timeUnit.toMillis(interval)) <= 0 ? 1 : (timeUnit.toMillis(interval)));
                    return acquire(ttl, interval, maxRetry - 1);
                }
            }
        } catch (Exception e) {
            if (maxRetry > 0) {
                try {
                    Thread.sleep((timeUnit.toMillis(interval)) <= 0 ? 1 : (timeUnit.toMillis(interval)));
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                return acquire(ttl, interval, maxRetry - 1);
            }
        }
        return hold.get();
    }

    /**
     * Release the lock use Java 7 try-with-resources.
     */
    @Override
    public void close() throws SimpleLockException {
        try {
            if (hold.get()) {
                release();
            }
        } catch (SimpleLockException ex) {
            throw ex;
        }
    }

    /**
     * 抢锁
     *
     * @return true-获取锁，false-未获得锁
     * @throws SimpleLockException 锁异常
     */
    protected abstract boolean lock() throws SimpleLockException;

    /**
     * 获取方案客户端 对象[redis,zookeeper,etcd]
     *
     * @return 解决方案 客户端 对象
     */
    protected abstract Object getClient();
}
