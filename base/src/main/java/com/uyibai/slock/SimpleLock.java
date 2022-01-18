package com.uyibai.slock;

import com.uyibai.slock.exception.SimpleLockException;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁 接口
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date : 2022/1/18
 */
public interface SimpleLock extends AutoCloseable {
    /**
     * Release the lock use Java 7 try-with-resources.
     */
    @Override
    void close() throws Exception;

    /**
     * 获取锁，如果没得到，不阻塞
     *
     * @param ttl 过期时间，ms
     * @return 成功 返回 true, 否则返回 false
     * @throws SimpleLockException 锁异常
     */
    boolean acquire(int ttl) throws SimpleLockException;

    /**
     * 获取锁，直到超时
     *
     * @param ttl      过期时间，单位ms
     * @param interval 重试间隔时间，单位ms
     * @param maxRetry 最大重试次数
     * @return 成功 返回 true, 否则返回 false
     * @throws SimpleLockException 锁异常
     */
    boolean acquire(int ttl, long interval, int maxRetry) throws SimpleLockException;

    /**
     * 释放锁
     */
    void release() throws SimpleLockException;

}
