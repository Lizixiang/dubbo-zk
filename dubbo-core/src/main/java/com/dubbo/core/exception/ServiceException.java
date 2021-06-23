package com.dubbo.core.exception;

/**
 * @author lizixiang
 * @since 2021/6/4
 */
public class ServiceException extends RuntimeException {

    private Object errorCode;
    private String message;

    public ServiceException(String message, ResultCode errorCode) {
        this.errorCode = errorCode;
        this.message = message;
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
}
