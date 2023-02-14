package com.dubbo.user.api.controller;

import com.dubbo.user.api.dto.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * <pre>
 * 此处填写中文类名
 * 此处填写中文类描述
 * </pre>
 *
 * @author lizixiang
 * @date 2023年02月13日
 */
@RestController
@RequestMapping("/test/locale")
public class TestLocaleController {

    @GetMapping("/test1")
    public void test1(@Validated User user, @NotBlank String message) {
        System.out.println(message);
    }

}
