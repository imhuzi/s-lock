package com.uyibai.slock.zookeeper.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Zookeeper 分布式锁 client
 */
public abstract class SlockZkClient {
    /**
     * 链接字符串
     */
    private String connectString = "172.16.5.152:2181";
    /**
     * 会话超时时间
     */
    private int sessionTimeoutMs = 3000;
    /**
     * 链接超时时间
     */
    private int connectionTimeoutMs = 3000;

    /**
     * 重试策略: 等待时间的基础单位，单位毫秒;
     */
    private int retryBaseSleepTimeMs = 1000;

    /**
     * 重试策略:  最大重试次数
     */
    private int retryMaxTimes = 3;

    /**
     * 重试策略: 等待时间的基础单位，单位毫秒; 最大重试次数
     */

    /**
     * Zookeeper client, 两种方式：newClient 和 builder 方法
     */
    //    private CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
    private final CuratorFramework client;

    public SlockZkClient(String connectString, int sessionTimeoutMs, int connectionTimeoutMs, int retryBaseSleepTimeMs, int retryMaxTimes) {
        this.connectString = connectString;
        this.sessionTimeoutMs = sessionTimeoutMs;
        this.connectionTimeoutMs = connectionTimeoutMs;
        this.retryBaseSleepTimeMs = retryBaseSleepTimeMs;
        this.retryMaxTimes = retryMaxTimes;
        this.client = getClient();
    }

    public SlockZkClient() {
        this.client = getClient();
    }

    private CuratorFramework getClient() {
        return CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .retryPolicy(new ExponentialBackoffRetry(retryBaseSleepTimeMs, retryMaxTimes))
                .build();
    }

}
