package com.dubbo.core.exception;

/**
 * @author lizixiang
 * @since 2021/6/4
 */
public enum ErrorCode implements ResultCode {

    SYSTEM_ERROR(1001, "系统错误"),
    PARAM_ERROR(1002, "参数错误"),
    ;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
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
