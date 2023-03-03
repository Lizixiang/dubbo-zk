package com.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

}
