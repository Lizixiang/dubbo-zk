package com.dubbo.user.rpc.service.impl;

import com.dubbo.core.annotation.DistributedLock;
import com.dubbo.core.exception.ServiceException;
import com.dubbo.user.exception.UserErrorCode;
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

    @Override
    public void dubboRpc() {
//        throw new Exception(UserErrorCode.AUTH_ERROR.getMessage());
//        throw new ServiceException("授权失败", UserErrorCode.AUTH_ERROR);
        int i = 1 / 0;
    }
}
