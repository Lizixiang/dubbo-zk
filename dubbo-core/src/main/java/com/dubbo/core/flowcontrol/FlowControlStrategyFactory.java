package com.dubbo.core.flowcontrol;

import com.dubbo.core.flowcontrol.enums.AlgorithmEnum;
import org.springframework.util.Assert;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lizixiang
 * @since 2022/4/8
 */
public final class FlowControlStrategyFactory {

    public static final ConcurrentHashMap<AlgorithmEnum, FlowControlStrategy> enums = new ConcurrentHashMap<>();

    public static void register(AlgorithmEnum anEnum, FlowControlStrategy strategy) {
        Assert.notNull(anEnum, "anEnum must be not empty...");
        Assert.notNull(strategy, "strategy must be not empty...");
        enums.put(anEnum, strategy);
    }

    public static FlowControlStrategy getFlowControlStrategy(AlgorithmEnum anEnum) {
        return enums.get(anEnum);
    }

}
