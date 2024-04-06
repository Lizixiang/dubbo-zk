package com.dubbo.core.cache;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:缓存实例配置参数，用于后续初始化缓存对象
 *
 * @author lizixiang
 * @date 2024-04-05 15:49:03
 */
public class CaffeinePropsHolder {

    private static final Logger logger = LoggerFactory.getLogger(CaffeinePropsHolder.class);

    private static final ConcurrentHashMap<String, CaffeineProps> caffeinePropsMap = new ConcurrentHashMap<>();

    /**
     * 提供给客户端使用，用于注册需要实例化的缓存实例配置参数，后续在使用@Cacheable等注解时，懒加载其真正实例对象
     *
     * @param key
     * @param initialCapacity
     * @param maximumSize
     * @param expireAfterWrite
     * @param expireAfterAccess
     * @param refreshAfterWrite
     * @param l2CacheExpire
     */
    public static void register(String key, int initialCapacity, long maximumSize, long expireAfterWrite,
                                long expireAfterAccess, long refreshAfterWrite, long l2CacheExpire) {
        caffeinePropsMap.put(key, new CaffeineProps(key, initialCapacity, maximumSize, expireAfterWrite,
                expireAfterAccess, refreshAfterWrite, l2CacheExpire));
        logger.info("缓存:{}配置注册成功", key);
    }

    public static CaffeineProps get(String key) {
        CaffeineProps caffeineProps = caffeinePropsMap.get(key);
        return caffeineProps;
    }

    @Data
    static class CaffeineProps {

        // 缓存key
        private String key;

        // 初始化容量
        private int initialCapacity;

        // 最大容量
        private long maximumSize;

        // 缓存key被写入后，如果没有继续被写入，自动移除缓存的时间，单位：秒
        private long expireAfterWrite;

        // 缓存key没有被读/写，自动移除缓存的时间，单位：秒
        private long expireAfterAccess;

        // 缓存key被写入后，多久自动刷新，单位：秒
        private long refreshAfterWrite;

        // 二级缓存过期时间，单位：秒
        private long l2CacheExpire;

        public CaffeineProps(String key, int initialCapacity, long maximumSize, long expireAfterWrite, long expireAfterAccess, long refreshAfterWrite, long l2CacheExpire) {
            this.key = key;
            this.initialCapacity = initialCapacity;
            this.maximumSize = maximumSize;
            this.expireAfterWrite = expireAfterWrite;
            this.expireAfterAccess = expireAfterAccess;
            this.refreshAfterWrite = refreshAfterWrite;
            this.l2CacheExpire = l2CacheExpire;
        }

    }
}
