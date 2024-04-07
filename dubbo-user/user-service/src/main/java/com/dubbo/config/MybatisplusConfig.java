package com.dubbo.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Author: zixli
 * Date: 2020/8/19 13:16
 * FileName: MybatisplusConfig
 * Description: mybatis-plus配置
 */
@EnableTransactionManagement(order = 998)
@Configuration
public class MybatisplusConfig {

    /**
     * 分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
