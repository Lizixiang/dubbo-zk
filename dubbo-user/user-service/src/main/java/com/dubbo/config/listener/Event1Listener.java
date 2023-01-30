package com.dubbo.config.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author lizixiang
 * @date 2023年01月29日
 */
@Component
public class Event1Listener implements ApplicationListener<Event1> {

    private static final Logger logger = LoggerFactory.getLogger(Event1Listener.class);

    @Override
    public void onApplicationEvent(Event1 event) {
        logger.info("Event1Listener onApplicationEvent...");
    }

}
