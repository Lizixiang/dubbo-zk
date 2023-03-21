package com.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author lizixiang
 * @since 2021/2/7
 */
@SpringBootApplication
public class StartApplication {

    public static void main(String[] args) {
//        Locale.setDefault(new Locale("zh", "cn"));
        SpringApplication.run(StartApplication.class, args);
    }

    @Bean(name = "messageSource")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:ApplicationResources");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
