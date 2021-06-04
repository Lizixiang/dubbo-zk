package com.dubbo.user.api.controller;

import com.dubbo.user.rpc.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lizixiang
 * @since 2021/3/30
 */
@RestController
@RequestMapping("/zk")
public class TestZkController {

    @Autowired
    private UserService userService;

    @GetMapping("/test1")
    public void test1() {
        userService.getUser(1);
    }

}
