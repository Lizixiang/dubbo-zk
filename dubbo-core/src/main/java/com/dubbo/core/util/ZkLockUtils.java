package com.dubbo.core.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lizixiang
 * @since 2021/3/5
 */
public class ZkLockUtils {

    private static Logger logger = LoggerFactory.getLogger(ZkLockUtils.class);

    private static String zkAddress = "172.20.1.112:2181";

    public static CuratorFramework client;

    private static ThreadLocal<Map> localMap = new ThreadLocal<>();

    static {
        ExponentialBackoffRetry backoffRetry = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(zkAddress, 60000, 15000, backoffRetry);
        client.start();
    }

    /**
     * 加锁
     *
     * @param key      zk znode路径
     * @param time     失效时间
     * @param timeUnit
     * @return
     */
    public static boolean lock(String key, long time, TimeUnit timeUnit) {
        InterProcessMutex mutex = getMutex(key);
        try {
            return mutex.acquire(time, timeUnit);
        } catch (Exception e) {
            logger.error("ZkLockUtils lock error reason:", e);
            return false;
        }
    }

    /**
     * 释放锁
     *
     * @param key zk znode路径
     */
    public static void unlcok(String key) {
        InterProcessMutex mutex = getMutex(key);
        try {
            mutex.release();
        } catch (Exception e) {
            logger.error("ZkLockUtils unlcok error reason:", e);
        }
    }

    /**
     * 判断当前线程是否锁住path(调用该方法必须为执行lock方法的线程，否则不生效)
     *
     * @param key
     * @return
     */
    public static boolean isAcquire(String key) {
        InterProcessMutex mutex = getMutex(key);
        try {
            return mutex.isOwnedByCurrentThread();
        } catch (Exception e) {
            logger.error("ZkLockUtils unlcok error reason:", e);
        }
        return false;
    }

    /**
     * 检查节点是否存在
     *
     * @param key
     * @return
     */
    public static Stat isExist(String key) {
        Stat stat = null;
        try {
            stat = client.checkExists().forPath(key);
        } catch (Exception e) {
            logger.error("ZkLockUtils isExist error reason:", e);
        }
        return stat;
    }

    /**
     * 获取mutex 放到ThreadLocal重复使用
     *
     * @param key
     * @return
     */
    private static InterProcessMutex getMutex(String key) {
        InterProcessMutex mutex = null;
        Map<String, InterProcessMutex> map = localMap.get();
        if (!ObjectUtils.isEmpty(map) && map.containsKey(key)) {
            mutex = map.get(key);
        }
        if (ObjectUtils.isEmpty(mutex)) {
            mutex = new InterProcessMutex(client, key);
        }
        if (ObjectUtils.isEmpty(map)) {
            map = new HashMap<String, InterProcessMutex>();
        }
        map.put(key, mutex);
        localMap.set(map);
        return mutex;
    }

}
