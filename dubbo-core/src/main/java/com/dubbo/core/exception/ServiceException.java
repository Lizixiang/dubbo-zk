package com.dubbo.core.exception;

/**
 * @author lizixiang
 * @since 2021/6/4
 */
public class ServiceException extends RuntimeException {

    private Object errorCode;
    private String message;
    private Object[] args;

    public ServiceException(ResultCode errorCode, Object... args) {
        this.errorCode = errorCode;
        this.args = args;
    }

    public ServiceException(String message, ResultCode errorCode) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ServiceException(ResultCode errorCode, String message, Object... args) {
        this.errorCode = errorCode;
        this.message = message;
        this.args = args;
    }

    public ResultCode getErrorCode() {
        return (ResultCode) errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object[] getArgs() {
        return args;
    }
}
