package com.dubbo.core.flowcontrol;

/**
 * @author lizixiang
 * @since 2022/4/8
 */
public interface FlowControlStrategy {

    void flowControl(FlowControlDto dto);

}
