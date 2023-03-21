package com.dubbo.user.api.controller;

import com.dubbo.core.exception.ErrorCode;
import com.dubbo.core.exception.Result;
import com.dubbo.core.exception.ServiceException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author lizixiang
 * @date 2023年03月21日
 */
@RestController
@RequestMapping("/test/exception")
public class TestExceptionController {

    @GetMapping("/test1")
    public Result test1(HttpServletRequest request, @NotNull Integer i) {
        if (i == 1) {
            throw new ServiceException(ErrorCode.SYSTEM_ERROR);
        } else if (i == 2) {
            throw new ServiceException(ErrorCode.SYSTEM_ERROR, "系统繁忙", null);
        } else if (i == 3) {
            throw new ServiceException(ErrorCode.DATA_NOT_EXISTS, i);
        } else if (i == 4) {
            throw new ServiceException(ErrorCode.PERMISSION_ERROR, i, request.getRequestURI());
        } else if (i == 5) {
            throw new ServiceException(ErrorCode.PARAM_ERROR, "用户[%s]无法访问资源路径:[%s]", i, request.getRequestURI());
        }
        return new Result();
    }

}
