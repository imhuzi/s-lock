package com.uyibai.slock.confg;

import lombok.Data;

import java.util.List;

/**
 * Simple Lock config info
 *
 * @author : Hui.Wang [huzi.wh@gmail.com]
 * @version : 1.0
 * @date : 2021/8/31
 */
@Data
public class SlockConfigVo {
    /**
     * redis, etcd, zookeeper 链接字符串
     */
    private String connectString = "172.16.5.152:2181";

    /**
     * 锁 服务提供者：redis, etcd, zookeeper
     */
    private String provider;
    private Long connectTimeout;
    private Long timeout;
    private Boolean ssl;

    /**
     * zookeeper curator 配置
     */
    private CuratorProperties zk;

    private EtcdProperties etcd;

    private RedisProperties redis;

    /**
     * redis properties class
     */
    @Data
    public static class RedisProperties {
        private Integer shutdownTimeout;
        private Integer poolMaxIdle;
        private Integer poolMinIdle;
        private Integer poolMaxActive;
        private Integer poolMaxWait;
        private Integer poolTimeBetweenEvictionRuns;
        private List<String> clusterNodes;
        private Integer clusterMaxRedirects;
    }

    /**
     * etcd properties class
     */
    @Data
    public static class EtcdProperties {
        private Integer maxRetryCount;
        private Long heartbeatSleepTimeMs;
    }

    /**
     * zookeeper curator class
     */
    @Data
    public static class CuratorProperties {
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

    }
}
