package com.dubbo.user.rpc.service.impl;

import com.dubbo.user.rpc.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

@Component
@DubboService
public class UserServiceImpl implements UserService {
    @Override
    public String getUser(Integer uid) {
        return "1";
    }
}
