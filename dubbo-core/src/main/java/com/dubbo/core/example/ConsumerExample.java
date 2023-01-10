package com.dubbo.core.example;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * <pre>
 *  accept
 *  andThen
 * </pre>
 *
 * @author lizixiang
 * @date 2023年01月10日
 */
public class ConsumerExample {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerExample.class);

    public static void main(String[] args) {
        String s = test("hello world", (a) -> log(a));
        System.out.println(s);

        test1("hello", (a) -> {
            log(a);
            a.setC("C");
        }, (a) -> log(a));
    }

    public static String test(String s, Consumer<A> consumer) {
        logger.info("s:{}", s);
        A a = new A();
        a.setB("B");
        consumer.accept(a);
        return s;
    }

    public static String test1(String s, Consumer<A> c1, Consumer<A> c2) {
        logger.info("s:{}", s);
        A a1 = new A();
        a1.setB("B");
        // 先执行c1,再执行c2
        c1.andThen(c2).accept(a1);
        return s;
    }

    public static void log(A a) {
        System.out.println(JSON.toJSONString(a));
    }

    @Data
    static class A {
        private String b;
        private String c;
    }

}
