package com.dubbo.core.annotation.vo;

/**
 * @author lizixiang
 * @since 2021/6/1
 */
public class RedisLockDefinitionHolder {

    /**
     * key
     */
    private String key;

    /**
     * 超时时间（秒）
     */
    private long lockTime;

    /**
     * 上次更新时间（毫秒）
     */
    private Long lastModifyTime;

    /**
     * 当前线程
     */
    private Thread currentThread;

    /**
     * 总共尝试次数
     */
    private int tryCount;

    /**
     * 当前尝试次数
     */
    private int currentTry;

    /**
     * 更新的时间周期（毫秒）,公式 = 加锁时间（转成毫秒） / 3
     */
    private Long modifyPeriod;

    public RedisLockDefinitionHolder(String key, Long lockTime, Long lastModifyTime, Thread currentThread, int tryCount) {
        this.key = key;
        this.lockTime = lockTime;
        this.lastModifyTime = lastModifyTime;
        this.currentThread = currentThread;
        this.tryCount = tryCount;
        this.modifyPeriod = lockTime * 1000 / 3;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getLockTime() {
        return lockTime;
    }

    public void setLockTime(long lockTime) {
        this.lockTime = lockTime;
    }

    public Long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Thread getCurrentThread() {
        return currentThread;
    }

    public void setCurrentThread(Thread currentThread) {
        this.currentThread = currentThread;
    }

    public int getTryCount() {
        return tryCount;
    }

    public void setTryCount(int tryCount) {
        this.tryCount = tryCount;
    }

    public int getCurrentTry() {
        return currentTry;
    }

    public void setCurrentTry(int currentTry) {
        this.currentTry = currentTry;
    }

    public Long getModifyPeriod() {
        return modifyPeriod;
    }

    public void setModifyPeriod(Long modifyPeriod) {
        this.modifyPeriod = modifyPeriod;
    }
}
