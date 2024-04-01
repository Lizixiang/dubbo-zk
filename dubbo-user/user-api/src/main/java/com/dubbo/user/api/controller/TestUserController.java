package com.dubbo.user.api.controller;

import com.dubbo.core.exception.Result;
import com.dubbo.core.permission.Permission;
import com.dubbo.user.service.CustomerService;
import com.dubbo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class TestUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/query")
    public Result query() {
        return Result.SUCCESS(userService.queryAll(1, "1"));
    }

    @Permission
    @GetMapping("customer/list")
    public Result list() {
        List<Long> list = customerService.list();
        return Result.SUCCESS(list);
    }

}
