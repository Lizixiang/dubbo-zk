package com.dubbo.core.cache;


import com.dubbo.core.util.RedisUtils;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Description:自定义缓存管理器
 * 将Caffeine和Redis同时放入spring缓存管理器
 * 实现CacheManager，通过名称获取缓存
 *
 * @author lizixiang
 * @date 2024-04-04 21:03:35
 */

public class CompositeCacheManager implements CacheManager {

    private static final Logger logger = LoggerFactory.getLogger(CompositeCacheManager.class);

    // 这里存放了多个Caffeine Cache实例，通过CacheProperties得到多个实例
    private static ConcurrentHashMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    private CacheProperties cacheProperties;

    private RedisUtils redisUtils;

    public CompositeCacheManager(CacheProperties cacheProperties, RedisUtils redisUtils) {
        this.cacheProperties = cacheProperties;
        this.redisUtils = redisUtils;
    }

    @SneakyThrows
    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        if (cache == null) {
            synchronized (this) {
                if (cacheMap.get(name) == null) {
                    CaffeinePropsHolder.CaffeineProps caffeineProps = CaffeinePropsHolder.get(name);
                    if (caffeineProps == null) {
                        throw new UnSupportedCacheNameException("cache [" + name + "] not support");
                    }
                    cache = new CaffeineRedisCache(name, getInstance(name), redisUtils, cacheProperties, caffeineProps.getL2CacheExpire());
                    logger.info("CaffeineRedisCache instance called [{}] is created", name);
                    cacheMap.put(name, cache);
                }
            }
        }
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }

    /**
     * 根据缓存key清空本地缓存
     *
     * @param name
     * @param key
     */
    public void clear(String name, String key) {
        Cache cache = cacheMap.get(name);
        if (cache == null) {
            return;
        }
        CaffeineRedisCache caffeineRedisCache = (CaffeineRedisCache) cache;
        caffeineRedisCache.clear(key);
    }

    /**
     * 初始化Caffeine实例
     *
     * @param name
     * @return
     * @throws UnSupportedCacheNameException
     */
    private com.github.benmanes.caffeine.cache.Cache<Object, Object> getInstance(String name) throws UnSupportedCacheNameException {
        CaffeinePropsHolder.CaffeineProps caffeineProps = CaffeinePropsHolder.get(name);
        if (caffeineProps == null) {
            throw new UnSupportedCacheNameException("cache [" + name + "] not support");
        }
        Caffeine<Object, Object> builder = Caffeine.newBuilder();
        if (caffeineProps.getInitialCapacity() > 0) {
            builder.initialCapacity(caffeineProps.getInitialCapacity());
        }
        if (caffeineProps.getMaximumSize() > 0) {
            builder.maximumSize(caffeineProps.getMaximumSize());
        }
        if (caffeineProps.getExpireAfterWrite() > 0) {
            builder.expireAfterWrite(caffeineProps.getExpireAfterWrite(), TimeUnit.SECONDS);
        }
        if (caffeineProps.getExpireAfterAccess() > 0) {
            builder.expireAfterAccess(caffeineProps.getExpireAfterAccess(), TimeUnit.SECONDS);
        }
        if (caffeineProps.getRefreshAfterWrite() > 0) {
            builder.refreshAfterWrite(caffeineProps.getRefreshAfterWrite(), TimeUnit.SECONDS);
        }
        builder.recordStats();
        return builder.build();
    }

}
