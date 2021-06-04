package com.dubbo.user.rpc.service.impl;

import com.dubbo.core.annotation.DistributedLock;
import com.dubbo.user.rpc.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

@Component
@DubboService
public class UserServiceImpl implements UserService {

    @DistributedLock(key = "/com/lzx/%s", lockField = "#uid")
    @Override
    public String getUser(Integer uid) {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "1";
    }
}
