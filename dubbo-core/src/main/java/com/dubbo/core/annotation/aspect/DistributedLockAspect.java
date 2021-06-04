package com.dubbo.core.annotation.aspect;

import com.dubbo.core.annotation.DistributedLock;
import com.dubbo.core.annotation.vo.RedisLockDefinitionHolder;
import com.dubbo.core.util.ZkLockUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * @author lizixiang
 * @since 2021/6/1
 */
@Aspect
@Component
public class DistributedLockAspect {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLockAspect.class);

    // 任务队列
    private static ConcurrentLinkedQueue<RedisLockDefinitionHolder> QUEUE = new ConcurrentLinkedQueue<>();

    // 线程池
    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(
            new BasicThreadFactory.Builder().namingPattern("DistributedLock-schedule-pool").daemon(true).build());

    {
        SCHEDULER.scheduleAtFixedRate(() -> {
            logger.info("开始扫描任务队列:{}", QUEUE);
            QUEUE.parallelStream().forEach(e -> {
                if (ObjectUtils.isEmpty(e)) {
                    return;
                }
                // 判断当前key是否有效，无效移除
                if (ObjectUtils.isEmpty(ZkLockUtils.isExist(e.getKey()))) {
                    QUEUE.remove(e);
                    return;
                }
                // 判断尝试延长超时时间次数是否大于指定次数，超时中断线程
                if (e.getCurrentTry() > e.getTryCount()) {
                    e.getCurrentThread().interrupt();
                    QUEUE.remove(e);
                    return;
                }
                // 如果超时时间已过三分之一，那么重设超时时间
                /**
                 * 这边代码注掉，zookeeer分布式锁不支持用任务线程来延迟锁的超时时间
                 * zookeeper锁是基于当前线程存储的，并且CuratorFrament没有提供延迟超时时间的api
                 * 换成redis分布式锁可以通过重设key的超时时间实现
                 */
//                long now = System.currentTimeMillis();
//                logger.info("now[{}] > e.getLastModifyTime()[{}] + e.getModifyPeriod()[{}]) : {}", now, e.getLastModifyTime(), e.getModifyPeriod(), now >= e.getLastModifyTime() + e.getModifyPeriod());
//                if (now >= e.getLastModifyTime() + e.getModifyPeriod()) {
//                    e.setLastModifyTime(now);
//                    ZkLockUtils.lock(e.getKey(), e.getLockTime(), TimeUnit.SECONDS);
//                    logger.info("[{}] retry count : {}", e.getKey(), e.getCurrentTry());
//                    e.setCurrentTry(e.getCurrentTry() + 1);
//                }
            });
        }, 0, 2, TimeUnit.SECONDS); // 2秒执行一次任务
    }

    @Pointcut("@annotation(com.dubbo.core.annotation.DistributedLock))")
    public void distributedLock() {
    }

    @Around(value = "distributedLock()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        DistributedLock annotation = method.getAnnotation(DistributedLock.class);
        String key = annotation.key();
        if (ObjectUtils.isEmpty(key)) {
            throw new Exception("key is empty...");
        }
        long lockTime = annotation.lockTime();
        String[] lockField = annotation.lockField();
        Object result = null;
        boolean lock = ZkLockUtils.lock(String.format(key, lockField), lockTime, TimeUnit.SECONDS);
        try {
            if (lock) {
                logger.info("[{}]加锁成功", String.format(key, lockField));
                // 防止超时，释放锁，导致数据并发异常
                Thread thread = Thread.currentThread();
                QUEUE.add(new RedisLockDefinitionHolder(String.format(key, lockField), lockTime, System.currentTimeMillis(),
                        thread, annotation.retryCount()));
                result = pjp.proceed();
            }
        } catch (Exception e) {
            logger.error("加锁失败:", e);
        } finally {
            ZkLockUtils.unlcok(key);
            logger.info("[{}]释放锁", String.format(key, lockField));
        }
        return result;
    }

}
