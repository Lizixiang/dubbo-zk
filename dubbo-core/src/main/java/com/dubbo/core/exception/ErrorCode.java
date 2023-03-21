package com.dubbo.core.exception;

/**
 * @author lizixiang
 * @since 2021/6/4
 */
public enum ErrorCode implements ResultCode {

    SYSTEM_ERROR(1001, "系统错误"),
    PARAM_ERROR(1002, "参数错误"),
    FLOW_CONTROL(1003, "触发限流"),
    DATA_NOT_EXISTS(1004, "数据id[{0}]不存在"),
    PERMISSION_ERROR(1005, "用户[{0}]无法访问资源路径:[{1}]"),
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
