package com.dubbo.config.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author lizixiang
 * @date 2023年02月08日
 */
@Component
public class EventListener1 {

    private static final Logger logger = LoggerFactory.getLogger(EventListener1.class);

    @EventListener
    public void test(Event1 event1) {
        logger.info("EventListener1.test...");
    }

}
