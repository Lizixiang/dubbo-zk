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
 * 滑动窗口
 * 具体逻辑：
 * 定义S秒内最大请求数为M，当前请求数N
 * 当N<M，缓存push最新请求以及时间
 * 当N>M，取最早的请求及时间，当前时间-最早的时间<S，则认为在S秒内请求数超过了M（关键点），触发限流，否则继续缓存push最新请求以及时间，并清掉缓存最早的那一条请求
 *
 * 优点：
 * 1.滑动窗口解决了计数器算法的临界值问题(不懂自行百度)，实现简单。
 * 2.可以解决少量流量突发的业务场景，但是流量突发多见于微博、直播、电商等行业内，且令牌桶算法更被行业认可
 *
 * 缺点：
 * 1.滑动窗口的时间复杂度为O(n)，因为这里采用list数据结构，查询、修改元素需要逐个查询链表指针，效率要比令牌桶算法低一些。
 * 2.滑动窗口需要存储更多的数据，存储N(请求阀值)个数据，过期时间为S(S为一个滑动窗口的计算周期)，随着窗口细粒度越高，流量峰值设的越高，存储的数据也就越大
 * @author lizixiang
 * @since 2022/4/8
 */
@Component
public class SlideWindowStrategy extends AbstractFlowControlStrategy {
    private static final Logger logger = LoggerFactory.getLogger(SlideWindowStrategy.class);

    @Autowired
    private RedisUtils redisUtils;

    @Override
    protected void doControl(FlowControlDto dto) {
        StringBuilder script = new StringBuilder();
        script.append("redis.replicate_commands()");
        script.append("\n local count = redis.call('llen', KEYS[1])");
        script.append("\n if count and tonumber(count) < tonumber(ARGV[1]) then");
        script.append("\n local now = redis.call('time')");
        script.append("\n redis.call('lpush', KEYS[1], now[1] * 1000000 + now[2])");
        script.append("\n redis.call('expire', KEYS[1], tonumber(ARGV[2]) + 1)");
        script.append("\n else");
        script.append("\n local now = redis.call('time')");
        script.append("\n local time = redis.call('lindex', KEYS[1], -1)");
        script.append("\n if now[1] * 1000000 + now[2] - time < tonumber(ARGV[2]) * 1000000 then");
        script.append("\n return -1");
        script.append("\n else");
        script.append("\n redis.call('lpush', KEYS[1], now[1] * 1000000 + now[2])");
        script.append("\n redis.call('expire', KEYS[1], tonumber(ARGV[2]) + 1)");
        script.append("\n redis.call('ltrim', KEYS[1], 0, tonumber(ARGV[1]) - 1)");
        script.append("\n end");
        script.append("\n end");
        script.append("\n return 1");
        long r = (long) redisUtils.eval(script.toString(), Lists.newArrayList(dto.getKey()), Lists.newArrayList(dto.getMax() + "", dto.getDuration() + ""));
        if (r == -1L) {
            logger.error("资源："+ dto.getKey() +"，在"+ dto.getDuration() +"秒内调用超过"+ dto.getMax() +"次，触发限流");
            throw new ServiceException("在"+ dto.getDuration() +"秒内调用超过"+ dto.getMax() +"次，触发限流", ErrorCode.FLOW_CONTROL);
        }
    }

    @PostConstruct
    public void init() {
        FlowControlStrategyFactory.register(AlgorithmEnum.SLIDE_WINDOW, this);
    }

}
