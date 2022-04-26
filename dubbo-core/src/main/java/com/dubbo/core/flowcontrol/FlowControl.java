package com.dubbo.core.flowcontrol;

import com.dubbo.core.flowcontrol.enums.AlgorithmEnum;
import com.dubbo.core.flowcontrol.enums.FlowControlEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限流
 * @author lizixiang
 * @since 2022/4/8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface FlowControl {

    /**
     * 时间段(s)
     */
    long duration() default 60;

    /**
     * 水流速率(/s)
     */
    int flowRate() default 1;

    /**
     * 最大访问数
     */
    int max() default 1000;

    /**
     * 限流方式
     */
    FlowControlEnum[] method() default FlowControlEnum.IP;

    /**
     * 自定义key
     */
    String key() default "" ;

    /**
     * 限流算法
     */
    AlgorithmEnum algorithm() default AlgorithmEnum.SLIDE_WINDOW;

}
