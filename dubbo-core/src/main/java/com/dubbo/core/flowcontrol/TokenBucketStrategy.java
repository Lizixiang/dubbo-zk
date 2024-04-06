package com.dubbo.core.flowcontrol;

import com.dubbo.core.exception.ErrorCode;
import com.dubbo.core.exception.ServiceException;
import com.dubbo.core.flowcontrol.enums.AlgorithmEnum;
import com.dubbo.core.util.LuaUtils;
import com.dubbo.core.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 令牌桶算法
 * 具体逻辑：
 * 定义桶容量M、当前令牌量C、令牌增速S
 * 单独起线程C每秒增加S，当C=M，不再继续增加
 * 当C>0，获取令牌，否则触发限流
 *
 * 优点：
 * 1.为了支持可配置多个令牌key，这里我们采用哈希结构，查找，修改元素只需要找到对应的key即可，时间复杂度为O(1)。
 * 2.令牌桶算法存储所需要的空间更小，存储令牌key以及当前令牌数
 * 3.令牌桶算法允许流量突发，因为拿令牌不需要耗费时间，适用于电商、微博、直播等业务
 *
 * 缺点：
 * 1.令牌桶算法是以一个恒定的速率发放令牌，达到阀值不再继续发放，如果在短时间内一批请求进来，可能会出现有的请求拿不到令牌，即触发了限流。
 * @author lizixiang
 * @since 2022/4/24
 * @return -1:触发限流 0:代表首次请求
 */
@Component
public class TokenBucketStrategy extends AbstractFlowControlStrategy{
    private static final Logger logger = LoggerFactory.getLogger(TokenBucketStrategy.class);

    private static ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);
    private static final String TOKEN_BUCKETS = "tokenbuckets-{wl}";

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private LuaUtils luaUtils;

    @PostConstruct
    public void init() {
        FlowControlStrategyFactory.register(AlgorithmEnum.TOKEN_BUCKET, this);
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                StringBuilder script = new StringBuilder();
                script.append("redis.replicate_commands()");
                script.append(luaUtils.buildSplitFunc());
                script.append("\n local map = redis.call('hgetall', KEYS[1])");
                script.append("\n for i = 1, #map, 2 do");
                script.append("\n   local field = map[i]");
                script.append("\n   local t = field:split('-')");
                script.append("\n   local max = t[2]");
                script.append("\n   local flowRate = t[3]");
                script.append("\n   local count = map[i+1]");
                script.append("\n   if count and tonumber(count) <= (tonumber(max) - tonumber(flowRate)) then");
                script.append("\n   redis.call('hincrBy', KEYS[1], field, tonumber(flowRate))");
                script.append("\n   end");
                script.append("\n end");
                script.append("\n return map");
                String fields = redisUtils.eval(script.toString(),  1, TOKEN_BUCKETS).toString();
//                logger.info("current token buckets:{}", fields);
            }
        }, 10, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void doControl(FlowControlDto dto) {
        StringBuilder script = new StringBuilder();
        script.append("redis.replicate_commands()");
        script.append("\n local count = redis.call('hget', KEYS[1], KEYS[2])");
        script.append("\n if count then");
        script.append("\n if tonumber(count) > 0 then");
        script.append("\n local incr = redis.call('hincrBy', KEYS[1], KEYS[2], -1)");
        script.append("\n return incr");
        script.append("\n else");
        script.append("\n return -1");
        script.append("\n end");
        script.append("\n else");
        script.append("\n redis.call('hset', KEYS[1], KEYS[2], 0)");
        script.append("\n return 0");
        script.append("\n end");
        long r = (long) redisUtils.eval(script.toString(), 2, new String[]{TOKEN_BUCKETS, (dto.getKey() + "-" + dto.getMax() + "-" + dto.getFlowRate() + "-{wl}")});
//        logger.info("current token buckets count:{}", r);
        if (r == -1L) {
            logger.warn("资源："+ dto.getKey() +"，调用超过"+ dto.getMax() +"次，触发限流");
            throw new ServiceException("资源："+ dto.getKey() +"，调用超过"+ dto.getMax() +"次，触发限流", ErrorCode.FLOW_CONTROL);
        }
    }

}
