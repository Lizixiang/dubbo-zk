package com.dubbo.user.consumer;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author lizixiang
 * @date 2024-04-15 13:51:25
 */
@Component
@RocketMQMessageListener(consumerGroup = "user-api", topic = "user", consumeMode = ConsumeMode.CONCURRENTLY, messageModel = MessageModel.CLUSTERING)
public class UserMqConsumer implements RocketMQListener<String> {

    private static final Logger logger = LoggerFactory.getLogger(UserMqConsumer.class);

    @Override
    public void onMessage(String message) {
        logger.info("UserMqConsumer receive message:{}", message);
        try {
            logger.info("UserMqConsumer end");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
