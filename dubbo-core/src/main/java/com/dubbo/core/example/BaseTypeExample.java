package com.dubbo.core.example;

import java.math.BigDecimal;

/**
 * Description:
 *
 * @author lizixiang
 * @date 2024-04-07 19:27:03
 */
public class BaseTypeExample {

    public static void main(String[] args) {
        Integer a = 127;
        Integer c = 127;
        System.out.println(a == c);
        Boolean b = false;
        System.out.println(b);
        System.out.println(a + 2);

        System.out.println(0.1 + 0.2);

        BigDecimal b1 = new BigDecimal("0.2");
        BigDecimal b2 = new BigDecimal("0.20");
        System.out.println(b1.compareTo(b2));
        System.out.println(b1.equals(b2));

        BigDecimal b3 = new BigDecimal(2);
        BigDecimal b4 = new BigDecimal(2.0);
        System.out.println(b3.equals(b4));

        BigDecimal b5 = new BigDecimal(0.2);
        BigDecimal b6 = new BigDecimal(0.20);
        System.out.println(b5.equals(b6));

        System.out.println(BigDecimal.valueOf(0.1));

        /**
         * 第①行，new 一个 String 对象，并让 s1指向他。
         * 第②行，对 s1执行 intern，但是因为"a"这个字符串已经在字符串池中，所以会直接返回原来的引用，但是并没有赋值给任何一个变量。
         * 第③行，s2指向常量池中的"a"；
         * 所以，s1和 s2并不相等！
         */
        String s1 = new String("a");
        s1.intern();
        String s2 = "a";
        System.out.println(s1 == s2);

        /**
         * 第①行，new 一个 String 对象，并让 s3 指向他。
         * 第②行，对 s3 执行 intern，但是目前字符串池中还没有"aa"这个字符串，于是会把<s3指向的String对象的引用>放入<字符串常量池>
         * 第③行，因为"aa"这个字符串已经在字符串池中，所以会直接返回原来的引用，并赋值给 s4；
         * 所以，s3和 s4 相等！
         */
        String s3 = new String("a") + new String("a");
        s3.intern();
        String s4 = "aa";
        System.out.println(s3 == s4);

        String ss = "ab";
        String ss2 = new String("ab");
        System.out.println(ss == ss2);
    }

}
