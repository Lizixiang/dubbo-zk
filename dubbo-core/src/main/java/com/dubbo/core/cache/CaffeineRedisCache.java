package com.dubbo.core.cache;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.dubbo.core.util.RedisUtils;
import com.github.benmanes.caffeine.cache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.NullValue;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 * caffeine作为一级缓存
 * redis作为二级缓存
 * 通过继承AbstractValueAdaptingCache抽象类，重写增删改查方法
 *
 * @author lizixiang
 * @date 2024-04-05 15:16:36
 */
public class CaffeineRedisCache extends AbstractValueAdaptingCache {

    private static final Logger logger = LoggerFactory.getLogger(CaffeineRedisCache.class);

    // 缓存实例的名称
    private String name;

    private Cache<Object, Object> caffeine;

    private RedisUtils redisUtils;

    // 缓存key的前缀
    private String prefix;

    private static final String DELIMITER = ":";

    private Map<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    /**
     * key
     */
    private static RedisSerializer<String> keySerializer = new StringRedisSerializer();
    /**
     * Object value
     */
    private static RedisSerializer<Object> valueSerializer = new GenericFastJsonRedisSerializer();

    // 二级缓存过期时间
    private long l2CacheExpire;

    public CaffeineRedisCache(String name, Cache<Object, Object> caffeine, RedisUtils redisUtils, CacheProperties cacheProperties, long l2CacheExpire) {
        super(cacheProperties.isAllowNullValues());
        this.name = name;
        this.caffeine = caffeine;
        this.redisUtils = redisUtils;
        this.prefix = cacheProperties.getPrefix();
        this.l2CacheExpire = l2CacheExpire > 0 ? l2CacheExpire : cacheProperties.getL2CacheExpire();
    }

    @Override
    protected Object lookup(Object key) {
        Object cacheKey = getKey(key);
        Object value = caffeine.getIfPresent(cacheKey);
        if (value != null) {
            logger.info("key[{}] get result from caffeine:{}", cacheKey, value);
            return value instanceof NullValue ? null : value;
        }

        value = deserializeVal(redisUtils.get(serializeKey(cacheKey.toString())));
        if (value != null) {
            logger.info("key[{}] get result from redis:{}", cacheKey, value);
            caffeine.put(cacheKey, value);
            return value;
        }
        return null;
    }

    @Override
    public ValueWrapper get(Object key) {
        return toValueWrapper(lookup(key));
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = lookup(key);
        if (value != null) {
            return (T) value;
        }
        ReentrantLock lock = lockMap.get(key.toString());
        if (lock == null) {
            lock = new ReentrantLock();
            lockMap.put(key.toString(), lock);
        }

        try {
            lock.lock();
            value = lookup(key);
            if (value != null) {
                return (T) value;
            }
            //执行原方法获得value
            value = valueLoader.call();
            Object storeValue = toStoreValue(value);
            put(key, storeValue);
            return (T) value;
        } catch (Exception e) {
            throw new ValueRetrievalException(key, valueLoader, e.getCause());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(Object key, Object value) {
        if (!super.isAllowNullValues() && value == null) {
            this.evict(key);
            return;
        }
        redisUtils.set(serializeKey(getKey(key).toString()), serializeVal(toStoreValue(value)));
        redisUtils.expire(serializeKey(getKey(key).toString()), l2CacheExpire);
        // TODO: 2024/4/6 通知(MQ或者redis发布订阅)其他节点清除缓存，清除方法：CompositeCacheManager.clear
    }

    @Override
    public void evict(Object key) {
        redisUtils.del(getKey(key).toString());
        caffeine.invalidate(getKey(key));
        // TODO: 2024/4/6 通知(MQ或者redis发布订阅)其他节点清除缓存，清除方法：CompositeCacheManager.clear
    }

    @Override
    public void clear() {

    }

    /**
     * 清空缓存key的本地数据
     *
     * @param key
     */
    public void clear(String key) {
        logger.info("key [{}] local cache cleared", key);
        caffeine.invalidate(getKey(key));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    private Object getKey(Object key) {
        String s = this.getName().concat(this.DELIMITER).concat(key.toString());
        return StringUtils.isBlank(this.prefix) ? s : this.prefix.concat(this.DELIMITER).concat(s);
    }

    private byte[] serializeKey(String key) {
        return keySerializer.serialize(key);
    }

    private String deserializeKey(byte[] key) {
        return keySerializer.deserialize(key);
    }

    private byte[] serializeVal(Object value) {
        return valueSerializer.serialize(value);
    }

    private Object deserializeVal(byte[] value) {
        return valueSerializer.deserialize(value);
    }

}
