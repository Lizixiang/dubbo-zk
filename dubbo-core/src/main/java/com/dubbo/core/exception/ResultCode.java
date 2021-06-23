package com.dubbo.core.exception;

/**
 * @author lizixiang
 * @since 2021/6/4
 */
public interface ResultCode {

    /**
     * 状态码
     * @return
     */
    int getCode();

    /**
     * 提示信息
     */
    String getMessage();

}
