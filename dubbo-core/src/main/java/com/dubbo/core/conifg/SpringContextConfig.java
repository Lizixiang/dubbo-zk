package com.dubbo.core.conifg;

import com.dubbo.core.holder.SpringApplicationContextHolder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author lizixiang
 * @date 2023年03月21日
 */
@Component
public class SpringContextConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContextHolder.setApplicationContext(applicationContext);
    }

}
