package com.dubbo.user.api.controller;

import com.dubbo.config.listener.Event1;
import com.dubbo.core.exception.Result;
import com.dubbo.core.design.liability.HandlerChainManager;
import com.dubbo.user.dto.request.BaseHandlerDto1;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
public class TestController implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @GetMapping("/test1")
    public void test1(String message) {
        System.out.println(message);
    }

    @Autowired
    private HandlerChainManager handlerChainManager;

    @PostMapping("/test2")
    public Result test2() {
        BaseHandlerDto1 baseHandlerDto1 = new BaseHandlerDto1();
        baseHandlerDto1.setClassNames(Lists.newArrayList("senstiveHandler", "signHandler"));
        Result o = handlerChainManager.executeHandle(baseHandlerDto1);
        return o;
    }

    @GetMapping("/test3")
    public Result test3() {
        this.publisher.publishEvent(new Event1(this));
        return Result.SUCCESS();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
