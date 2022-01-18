package com.uyibai.slock.zookeeper.lock;

import com.uyibai.slock.BaseSimpleLock;
import com.uyibai.slock.exception.SimpleLockException;

import java.util.concurrent.TimeUnit;

/**
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date : 2022/1/18
 */
public class ZookeeperLock extends BaseSimpleLock {

    public ZookeeperLock(String key, String value, TimeUnit timeUnit, boolean reentrant) {
        super(key, value, timeUnit, reentrant);
    }

    /**
     * 抢锁
     *
     * @return true-获取锁，false-未获得锁
     * @throws SimpleLockException 锁异常
     */
    @Override
    protected boolean lock() throws SimpleLockException {
        return false;
    }

    /**
     * 获取方案客户端 对象[redis,zookeeper,etcd]
     *
     * @return 解决方案 客户端 对象
     */
    @Override
    protected Object getClient() {
        return null;
    }

    /**
     * 释放锁
     */
    @Override
    public void release() throws SimpleLockException {

    }
}
