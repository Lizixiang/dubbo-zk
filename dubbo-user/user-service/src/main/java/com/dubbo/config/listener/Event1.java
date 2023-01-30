package com.dubbo.config.listener;

import org.springframework.context.ApplicationEvent;

/**
 *
 * @author lizixiang
 * @date 2023年01月29日
 */
public class Event1 extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public Event1(Object source) {
        super(source);
    }

}
