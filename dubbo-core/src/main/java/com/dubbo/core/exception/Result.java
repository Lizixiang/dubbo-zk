package com.dubbo.core.exception;

import com.dubbo.core.util.MessageUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lizixiang
 * @since 2021/6/4
 */
public class Result {

    int code = 0;

    String message;

    Object data;

    boolean success = true;

    public Result() {
    }

    public Result(int code, String message, Object data, boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    public static Result SUCCESS() {
        return new Result();
    }

    public static Result SUCCESS(Object data) {
        Result result = new Result();
        result.setData(data);
        return result;
    }

    public static Result ERROR(String message) {
        Result result = new Result();
        result.setCode(-1);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }

    public static Result ERROR(ResultCode e) {
        Result result = new Result();
        result.setCode(e.getCode());
        result.setMessage(MessageUtils.getMessage(e));
        result.setSuccess(false);
        return result;
    }

    public static Result ERROR(ServiceException e) {
        Result result = new Result();
        result.setCode(e.getErrorCode().getCode());
        if (StringUtils.isNotBlank(e.getMessage())) {
            result.setMessage(String.format(e.getMessage(), e.getArgs()));
        } else {
            result.setMessage(MessageUtils.getMessage(e.getErrorCode(), e.getArgs()));
        }
        result.setSuccess(false);
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
