package com.dubbo.user.enums;


/**
 * Description:根据业务场景定义不同的缓存配置
 *
 * @author lizixiang
 * @date 2024-04-04 21:45:55
 */
public enum CacheManagerEnum {

    USER("user", 128, 1024, 900000l, 900000l, 0l, 1800l),
    ;

    CacheManagerEnum(String key, int initialCapacity, long maximumSize, Long expireAfterWrite, Long expireAfterAccess, Long refreshAfterWrite, Long l2CacheExpire) {
        this.key = key;
        this.initialCapacity = initialCapacity;
        this.maximumSize = maximumSize;
        this.expireAfterWrite = expireAfterWrite;
        this.expireAfterAccess = expireAfterAccess;
        this.refreshAfterWrite = refreshAfterWrite;
        this.l2CacheExpire = l2CacheExpire;
    }

    // 缓存key
    private String key;

    // 初始化容量
    private int initialCapacity;

    // 最大容量
    private long maximumSize;

    // 缓存key被写入后，如果没有继续被写入，自动移除缓存的时间
    private Long expireAfterWrite;

    // 缓存key没有被读/写，自动移除缓存的时间
    private Long expireAfterAccess;

    // 缓存key被写入后，多久自动刷新
    private Long refreshAfterWrite;

    // 二级缓存过期时间，单位：秒
    private long l2CacheExpire;

    public String getKey() {
        return key;
    }

    public int getInitialCapacity() {
        return initialCapacity;
    }

    public long getMaximumSize() {
        return maximumSize;
    }

    public Long getExpireAfterWrite() {
        return expireAfterWrite;
    }

    public Long getExpireAfterAccess() {
        return expireAfterAccess;
    }

    public Long getRefreshAfterWrite() {
        return refreshAfterWrite;
    }

    public long getL2CacheExpire() {
        return l2CacheExpire;
    }
}
