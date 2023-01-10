package com.dubbo.core.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;

/**
 * <pre>
 * 此处填写中文类名
 * 此处填写中文类描述
 * </pre>
 *
 * @author lizixiang
 * @date 2023年01月09日
 */
@Configuration
public class FilterConfiguration {

    @Autowired
    private RiskFilter riskFilter;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(riskFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("riskFilter");
        filterRegistrationBean.setOrder(3);
        return filterRegistrationBean;
    }

}
