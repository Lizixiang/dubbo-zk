package com.dubbo.core.exception;

import com.dubbo.core.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lizixiang
 * @since 2021/6/4
 */
@RestControllerAdvice
public class CommonExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result exceptionHandle(HttpServletRequest request, ServiceException e) {
        logger.error("do [{}] on [{}] fail: {}", request.getMethod(), request.getRequestURL(), ExceptionUtils.getStackTrace(e));
        if (logger.isDebugEnabled()) {
            logger.error("queryString[{}] parameter[{}] error : {}", request.getQueryString(), request.getParameterMap(), ExceptionUtils.getStackTrace(e));
        }
        if (e instanceof ResultCode) {
            return Result.ERROR(e.getErrorCode());
        } else {
            return Result.ERROR(e.getMessage());
        }
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result exceptionHandle(HttpServletRequest request, BindException e) {
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : e.getFieldErrors()) {
            sb.append("[" + fieldError.getObjectName() + "." + fieldError.getField() + "]" + fieldError.getDefaultMessage() + "  ");
        }
        logger.error("do [{}] on [{}] fail: {}", request.getMethod(), request.getRequestURL(), sb);
        if (logger.isDebugEnabled()) {
            logger.error("queryString[{}] parameter[{}] error : {}", request.getQueryString(), request.getParameterMap(), ExceptionUtils.getStackTrace(e));
        }
        return Result.ERROR(ErrorCode.PARAM_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exceptionHandle(HttpServletRequest request, Exception e) {
        logger.error("do [{}] on [{}] fail: {}", request.getMethod(), request.getRequestURL(), ExceptionUtils.getStackTrace(e));
        if (logger.isDebugEnabled()) {
            logger.error("queryString[{}] parameter[{}] error : {}", request.getQueryString(), request.getParameterMap(), ExceptionUtils.getStackTrace(e));
        }
        return Result.ERROR(ErrorCode.SYSTEM_ERROR);
    }

}
