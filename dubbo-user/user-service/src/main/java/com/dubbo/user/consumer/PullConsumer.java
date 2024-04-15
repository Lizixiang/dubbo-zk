package com.dubbo.user.consumer;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author lizixiang
 * @date 2024-04-15 17:58:17
 */
@Component
public class PullConsumer {

    private static final Logger logger = LoggerFactory.getLogger(PullConsumer.class);

    private DefaultLitePullConsumer pullConsumer = null;

    @PostConstruct
    public void init() {
        pullConsumer = new DefaultLitePullConsumer();
        pullConsumer.setNamesrvAddr("localhost:9876");
        pullConsumer.setConsumerGroup("user-api");// group
        pullConsumer.setPullBatchSize(1);// 拉取数量
        pullConsumer.setPollTimeoutMillis(10 * 1000);// 10s
        pullConsumer.setMessageModel(MessageModel.BROADCASTING);
        try {
            pullConsumer.start();
            pullConsumer.subscribe("user");
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }

        ThreadPoolExecutor singleExecutor = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        singleExecutor.submit(() -> {
            while (true) {
                List<MessageExt> messages = pullConsumer.poll();
                if (messages != null) {
                    for (MessageExt message : messages) {
                        try {
                            logger.info("PullConsumer receive message:{}", message.getBody());
                            String msg = new String(message.getBody(), Charset.forName("UTF-8"));
                            logger.info("PullConsumer receive msg:{}", msg);
                        } catch (Exception e) {
                            logger.error("PullConsumer consume error:{}", ExceptionUtils.getStackTrace(e));
                            continue;
                        }
                    }
                }
            }
        });
    }


}
