package com.dubbo.user.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * 此处填写中文类名
 * 此处填写中文类描述
 * </pre>
 *
 * @author lizixiang
 * @date 2023年01月06日
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/test1")
    public void test1(String message) {
        System.out.println(message);
    }

}
