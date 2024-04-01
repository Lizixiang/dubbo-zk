package com.dubbo.user.api.controller;

import com.dubbo.core.exception.Result;
import com.dubbo.core.exception.ServiceException;
import com.dubbo.user.dto.request.TestDto;
import com.dubbo.user.exception.UserErrorCode;
import com.dubbo.user.rpc.UserRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
    private UserRpcService userRpcService;

    @GetMapping("/test1")
    public void test1() {
        userRpcService.getUser(1);
    }

    @GetMapping("/validated")
    public Result validated(@Validated TestDto dto) {
        return new Result();
    }

    @GetMapping("/throwException")
    public Result throwException() {
        if (true) {
            throw new ServiceException("授权失败", UserErrorCode.AUTH_ERROR);
        }
        return new Result();
    }

}
