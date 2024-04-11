package com.dubbo.user.api.controller;

import com.dubbo.core.exception.Result;
import com.dubbo.core.permission.Permission;
import com.dubbo.user.entity.SysUser;
import com.dubbo.user.entity.TUser;
import com.dubbo.user.service.CustomerService;
import com.dubbo.user.service.TUserService;
import com.dubbo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class TestUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    TUserService tUserService;

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

    @GetMapping("/add")
    public Result add() {
        List<TUser> list = new ArrayList<>();
        list.add(new TUser("1", "1", 1));
        list.add(new TUser("2", "2", 2));
        list.add(new TUser("3", "3", 3));
        tUserService.saveBatch(list);
        return Result.SUCCESS();
    }

    @GetMapping("list")
    public Result list1() {
        return Result.SUCCESS(tUserService.list());
    }

    @GetMapping("page")
    public Result page() {
        return Result.SUCCESS(tUserService.page());
    }

}
