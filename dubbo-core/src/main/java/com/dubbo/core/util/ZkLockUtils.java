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
 * zookeeper和redis作为分布式锁的区别：
 *  1.底层存储的数据结构：
 *  redis可以使用string、hash、set、zset、list，并且运行在内存中，所以redis在性能方面优于zookeeper；
 *  zookeeper类似于windows的文件系统，是一个树状的目录结构，创建的是一个临时节点；
 *  2.锁是否有过期时间：
 *  zookeeper不存在过期时间，客户端和zookeeper建立连接后维护一个session，定时发送心跳，如果客户端没有应答，直接删除znode；
 *  redis需要设置过期时间，并且存在死锁，过期时间超时等问题；
 *  3.锁竞争时的策略：
 *  zookeeper：如果竞争中没有拿到锁，此时创建一个watcher，并使当前线程进入等待状态，直到锁被释放，调用notifyAll继续循环获取锁；
 *  redis：如果竞争中没有拿到锁，在超时时间之前循环拿锁，直到超时或者拿到锁；
 *
 * @author lizixiang
 * @since 2021/3/5
 */
public class ZkLockUtils {

    private static Logger logger = LoggerFactory.getLogger(ZkLockUtils.class);

    private static String zkAddress = "127.0.0.1:2181";

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
