package com.dubbo.core.flowcontrol;

/**
 * @author lizixiang
 * @since 2022/4/8
 */
public abstract class AbstractFlowControlStrategy implements FlowControlStrategy{

    @Override
    public void flowControl(FlowControlDto dto) {
        doControl(dto);
    }

    protected abstract void doControl(FlowControlDto dto);
}
