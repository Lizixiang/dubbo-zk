package com.dubbo.core.holder;

import org.springframework.context.ApplicationContext;

/**
 * @author lizixiang
 * @date 2023年03月21日
 */
public class SpringApplicationContextHolder {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringApplicationContextHolder.applicationContext = applicationContext;
    }
}
