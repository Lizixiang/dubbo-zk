package com.dubbo.user.exception;

import com.dubbo.core.exception.ResultCode;

/**
 * @author lizixiang
 * @since 2021/6/4
 */
public enum UserErrorCode implements ResultCode {

    AUTH_ERROR(2001, "授权失败"),
    ;

    private int code;

    private String message;

    UserErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
