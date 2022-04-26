package com.dubbo.core.flowcontrol;

import com.dubbo.core.exception.ErrorCode;
import com.dubbo.core.exception.ServiceException;
import com.dubbo.core.flowcontrol.enums.AlgorithmEnum;
import com.dubbo.core.util.RedisUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 漏桶算法
 * 具体逻辑：
 * 定义水流速S、水桶容量L、当前水量C
 * 当C>L,返回-1，触发限流
 * 当C<L,首先缓存取上一次请求的时间，(当前时间（秒）-上一次请求时间（秒）)*S = 流水量
 * 将redis list通过ltrim裁剪已流出的水量，并将当前请求push到缓存当中
 * L/S代表漏桶流空所需的时间，并将其设为过期时间
 *
 * 优点：
 * 1.漏桶算法不允许流量突发，一般适用于银行金融项目或者调取第三方api
 *
 * 缺点：
 * 1.漏桶算法的时间复杂度为O(n)，因为这里采用list数据结构，查询、修改元素需要逐个查询链表指针，效率要比令牌桶算法低一些。
 * 2.漏桶算法是以一个恒定的速率出水，瞬时进来一批请求，如果进水量大于漏桶容量则溢出。多余的请求触发限流。
 * 3.漏桶算法需要存储更多的数据，存储N(请求阀值)个数据，过期时间为水桶容量L/水流速S
 * @author lizixiang
 * @since 2022/4/8
 */
@Component
public class LeakyBucketStrategy extends AbstractFlowControlStrategy {
    private static final Logger logger = LoggerFactory.getLogger(LeakyBucketStrategy.class);

    @Autowired
    private RedisUtils redisUtils;

    @Override
    protected void doControl(FlowControlDto dto) {
        StringBuilder script = new StringBuilder();
        script.append("redis.replicate_commands()");
        script.append("\n local count = redis.call('llen', KEYS[1])");
        script.append("\n if count and tonumber(count) < tonumber(ARGV[1]) then");
        script.append("\n local now = redis.call('time')");
        script.append("\n if count and tonumber(count) > 0 then");
        script.append("\n local time = redis.call('lindex', KEYS[1], tonumber(count) - 1)");
        script.append("\n local t = ((now[1] - tonumber(time)) * ARGV[2])");
        script.append("\n redis.call('ltrim', KEYS[1], tonumber(t), tonumber(count) - 1)");
        script.append("\n end");
        script.append("\n redis.call('lpush', KEYS[1], now[1])");
        script.append("\n redis.call('expire', KEYS[1], (ARGV[1] / tonumber(ARGV[2])) + 1)");
        script.append("\n else");
        script.append("\n return -1");
        script.append("\n end");
        script.append("\n return 1");
        long r = (long) redisUtils.eval(script.toString(), Lists.newArrayList(dto.getKey()), Lists.newArrayList(dto.getMax() + "", dto.getFlowRate() + ""));
        if (r == -1L) {
            logger.error("资源："+ dto.getKey() +"，调用超过"+ dto.getMax() +"次，触发限流");
            throw new ServiceException("资源："+ dto.getKey() +"，调用超过"+ dto.getMax() +"次，触发限流", ErrorCode.FLOW_CONTROL);
        }
    }


    @PostConstruct
    public void init() {
        FlowControlStrategyFactory.register(AlgorithmEnum.LEAKY_BUCKET, this);
    }

}
