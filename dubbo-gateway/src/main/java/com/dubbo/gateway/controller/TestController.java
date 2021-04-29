package com.dubbo.gateway.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.dubbo.core.util.ZkLockUtils;
import com.dubbo.gateway.config.GateWayZkConfig;
import com.dubbo.user.rpc.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author lizixiang
 * @since 2021/2/7
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private static Logger logger = LoggerFactory.getLogger(TestController.class);

    public static List<Integer> list = new ArrayList<Integer>();

    private static Random r = new Random();

    @Reference
    private UserService userService;
    @Autowired
    private GateWayZkConfig gateWayZkConfig;

    @GetMapping("/user")
    public String getUser() {
        String user = userService.getUser(1);
        return user;
    }

    @GetMapping("/testzk")
    public void testzk(String key) {
        boolean lock = ZkLockUtils.lock(key, 30, TimeUnit.SECONDS);
        try {
            if (lock) {
                int i = r.nextInt(10);
                if (!list.contains(i)) {
                    list.add(i);
                }
                logger.info("testzk:" + JSON.toJSONString(list));
            }
        } catch (Exception e) {
            logger.error("testzk error:", e);
        } finally {
            ZkLockUtils.unlcok(key);
        }
    }

    @GetMapping("zkConfig")
    public String zkConfig() {
        return gateWayZkConfig.getSex();
    }

}
