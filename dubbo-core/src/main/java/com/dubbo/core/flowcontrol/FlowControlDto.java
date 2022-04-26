package com.dubbo.core.flowcontrol;

import com.dubbo.core.flowcontrol.enums.AlgorithmEnum;
import com.dubbo.core.flowcontrol.enums.FlowControlEnum;
import lombok.Builder;

import java.io.Serializable;

/**
 * @author lizixiang
 * @since 2022/4/8
 */
@Builder
public class FlowControlDto implements Serializable {

    /**
     * 时间段(s)
     */
    private long duration;

    /**
     * 水流速率(/s)
     */
    private int flowRate;

    /**
     * 最大访问数
     */
    private int max;

    /**
     * 限流方式
     */
    private FlowControlEnum method;

    /**
     * 自定义key
     */
    private String key;

    /**
     * 限流算法
     */
    private AlgorithmEnum algorithm;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public FlowControlEnum getMethod() {
        return method;
    }

    public void setMethod(FlowControlEnum method) {
        this.method = method;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public AlgorithmEnum getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(AlgorithmEnum algorithm) {
        this.algorithm = algorithm;
    }

    public int getFlowRate() {
        return flowRate;
    }

    public void setFlowRate(int flowRate) {
        this.flowRate = flowRate;
    }
}
