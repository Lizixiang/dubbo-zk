package com.dubbo.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lizixiang
 * @since 2021/6/1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DistributedLock {

    /**
     * 参数
     */
    String[] lockField() default "";

    /**
     * 超时重试次数
     */
    int retryCount() default 3;

    /**
     * 自定义key
     */
    String key() default "";

    /**
     * 超时时间（秒）
     */
    long lockTime() default 30;

}
