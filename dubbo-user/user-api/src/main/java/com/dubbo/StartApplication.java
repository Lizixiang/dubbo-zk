package com.dubbo;

import org.apache.ibatis.logging.LogFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author lizixiang
 * @since 2021/2/7
 */
@EnableCaching
@SpringBootApplication
@MapperScan("com.dubbo.user.mapper")
public class StartApplication {

    public static void main(String[] args) {
//        Locale.setDefault(new Locale("zh", "cn"));
        LogFactory.useLog4J2Logging();
        SpringApplication.run(StartApplication.class, args);
    }

//    @Bean(name = "messageSource")
//    public MessageSource messageSource() {
//        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//        messageSource.setBasename("classpath:ApplicationResources");
//        // 每10s刷新一次内存从properties中重新读取
//        messageSource.setCacheSeconds(10);
//        messageSource.setDefaultEncoding("UTF-8");
//        return messageSource;
//    }

}
