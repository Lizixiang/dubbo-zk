package com.dubbo.user.api.controller;

import com.dubbo.core.exception.Result;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:消息队列
 *
 * @author lizixiang
 * @date 2024-04-15 13:12:51
 */
@RestController
@RequestMapping("/mq")
public class TestMQController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/test1")
    public Result test1() {
        rocketMQTemplate.convertAndSend("user", "hello world");
        return Result.SUCCESS();
    }

    @GetMapping("/test2")
    public Result test2() {
        for (int i = 0; i < 10; i++) {
            rocketMQTemplate.convertAndSend("user", "hello world" + i);
        }
        return Result.SUCCESS();
    }

    @GetMapping("/test3")
    public Result test3() {
        try {
            DefaultMQProducer producer = new DefaultMQProducer("user-api");
            producer.setNamesrvAddr("localhost:9876");
            producer.setRetryTimesWhenSendFailed(3);
            producer.start();

            Message message = new Message("user", "Hello RocketMQ".getBytes());
            message.setDelayTimeMs(10000);// 延迟毫秒，最大不超过3天
            producer.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.SUCCESS();
    }

    @GetMapping("/test4")
    public Result test4() {
        try {
            DefaultMQProducer producer = new DefaultMQProducer("user-api");
            producer.setNamesrvAddr("localhost:9876");
            producer.setRetryTimesWhenSendFailed(3);
            producer.start();

            Message message = new Message("user", "Hello RocketMQ".getBytes());
            producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    // arg取模，将同一个id的消息放入同一个queue中，保证数据的顺序性
                    Integer a = arg != null ? (Integer) arg : 0;
                    int i = a % mqs.size();
                    return mqs.get(i);
                }
            }, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.SUCCESS();
    }

}
