package com.dubbo.user.api.controller;

import com.dubbo.core.flowcontrol.FlowControl;
import com.dubbo.core.flowcontrol.enums.AlgorithmEnum;
import com.dubbo.core.flowcontrol.enums.FlowControlEnum;
import com.dubbo.core.util.RedisUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @author lizixiang
 * @since 2022/4/8
 */
@RestController
@RequestMapping("/redis")
public class TestRedisController {

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("/setKey")
    public Object setKey(String key, String value) {
        String r = redisUtils.set(key, value);
        ArrayList<String> keys = Lists.newArrayList("11111");
        ArrayList<String> args = Lists.newArrayList("33333");
        Object eval = redisUtils.eval("return redis.call('set', KEYS[1], ARGV[1])", keys, args);
        Object eval2 = redisUtils.eval("return redis.call('get', KEYS[1])", 1, "11111");
        return eval2;
    }

    @FlowControl(key = "testSlideWindow", method = FlowControlEnum.KEY, max = 10, duration = 60)
    @GetMapping("/testSlideWindow")
    public void testSlideWindow() {
        System.out.println(10);
    }

    @FlowControl(key = "testLeakyBucket", method = FlowControlEnum.KEY, algorithm = AlgorithmEnum.LEAKY_BUCKET, max = 10, flowRate = 1)
    @GetMapping("/testLeakyBucket")
    public void testLeakyBucket() {
        System.out.println(11);
    }

    @FlowControl(key = "testTokenBucket", method = FlowControlEnum.KEY, algorithm = AlgorithmEnum.TOKEN_BUCKET, max = 10, flowRate = 1)
    @GetMapping("/testTokenBucket")
    public String testTokenBucket() {
        return "success";
    }

    @FlowControl(key = "testTokenBucket1", method = FlowControlEnum.KEY, algorithm = AlgorithmEnum.TOKEN_BUCKET, max = 20, flowRate = 2)
    @GetMapping("/testTokenBucket1")
    public String testTokenBucket1() {
        return "success";
    }

}
