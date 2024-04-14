package com.dubbo.user.rpc.service.impl;

import com.dubbo.core.annotation.DistributedLock;
import com.dubbo.core.exception.ServiceException;
import com.dubbo.user.entity.SysUser;
import com.dubbo.user.exception.UserErrorCode;
import com.dubbo.user.mapper.UserMapper;
import com.dubbo.user.rpc.UserRpcService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DubboService(methods = {@Method(name = "getUser", async = true)})
public class UserRpcServiceImpl implements UserRpcService {

    @Autowired
    private UserMapper userMapper;

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
        throw new ServiceException("授权失败", UserErrorCode.AUTH_ERROR);
//        int i = 1 / 0;
    }

    @Override
    public List queryAll() {
        return userMapper.selectList(null);
    }
}
