package com.dubbo.core.cache;

/**
 * Description:未查到客户端通过CaffeinePropsHolder.register方法注册的缓存key
 *
 * @author lizixiang
 * @date 2024-04-06 13:53:24
 */
public class UnSupportedCacheNameException extends Exception {

    private static final long serialVersionUID = 259712729218687636L;

    public UnSupportedCacheNameException(String message) {
        super(message);
    }

}
