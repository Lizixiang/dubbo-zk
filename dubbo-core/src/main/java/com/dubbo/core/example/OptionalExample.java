package com.dubbo.core.example;

import com.alibaba.fastjson.JSON;

import java.util.Optional;

/**
 * @author lizixiang
 * @since 2022/3/31
 */
public class OptionalExample {

    public static void main(String[] args) {
        testOfNullable();
        testOrElseGet();
        testFlatMap();
        testPresent();
        testFilter();
        testEquals();
    }

    /**
     * Optional.of方法基本不用 当入参为null时，会抛NullPointerException
     */
    public static void testOfNullable() {
        Optional<Object> o = Optional.ofNullable(null);
        Optional<Object> o1 = Optional.of(1);
    }

    /**
     * get()方法会抛NullPointerException
     * orElse(a)方法 当ofNullable入参为空时，取a值
     * orElseGet(() -> {}) 提供lambda语法
     */
    public static void testOrElseGet() {
        Object o = Optional.ofNullable(2).get();
        System.out.println(o);
        Object o1 = Optional.ofNullable(null).orElse(new test("1", "2"));
        System.out.println(JSON.toJSON(o1));
        test test = null;
        test = Optional.ofNullable(test).orElseGet(() -> {
            test test1 = new test("1", "2");
            return test1;
        });
        test test2 = null;
        // 不优雅的写法举例
        if (test2 == null) {
            test2 = new test("1", "2");
        }
        System.out.println(JSON.toJSON(test));
        System.out.println(JSON.toJSON(test2));
        System.out.println(test.getA().equals(test2.getA()));
    }

    /**
     * map和flatMap都会返回一个Optional<T>对象
     * map入参比flatMap入参多包了一层Optional.ofNullable
     */
    public static void testFlatMap() {
//        test test = new test(null, "2");
        test test = null;
        String s = Optional.ofNullable(test).map(OptionalExample.test::getA).orElse("3");
        String s1 = Optional.ofNullable(test).flatMap(e -> Optional.ofNullable(e.getA())).orElse("3");
        System.out.println("s:" + s);
        System.out.println("s1:" + s1);
        // 不优雅的写法举例
        String s2 = null;
        if (test != null) {
            if (test.getA() != null) {
                s2 = test.getA();
            } else {
                s2 = "3";
            }
        } else {
            s2 = "3";
        }
        System.out.println("s2:" + s2);
        System.out.println(s1.equals(s2));
    }

    /**
     * Optional.ofNullable().isPresent感觉很鸡肋，直接obj != null不好么
     */
    public static void testPresent() {
        test test = new test("1", "2");
        Optional.ofNullable(test).map(e -> e.getA()).ifPresent(e -> {
            System.out.println(e);
        });
    }

    /**
     * filter过滤为空返回Optional.EPMTY
     */
    public static void testFilter() {
        test test = new test("1", "2");
        Optional.ofNullable(test).filter(e -> "1".equals(e.getA())).ifPresent(a -> {
            System.out.println("a:" + JSON.toJSONString(a));
        });
    }

    /**
     * equals入参必须为Optional，否则返回false
     */
    public static void testEquals() {
        Integer a = 1;
        boolean equals = Optional.ofNullable(a).equals(Optional.of(1));
        boolean equals1 = Optional.ofNullable(a).equals(1);
        System.out.println("equals:" + equals);
        System.out.println("equals1:" + equals1);
    }

    static class test {
        private String a;
        private String b;

        public test(String a, String b) {
            this.a = a;
            this.b = b;
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }
    }

}
