package com.dubbo.core.example;

/**
 * Description:
 *
 * @author lizixiang
 * @date 2024-04-07 22:37:52
 */
public class ExceptionExample {

    public static void main(String[] args) {
        System.out.println(A());
        System.out.println(B());
    }

    static String A() {
        try {
            return "A";
        } catch (Exception e) {
            return "B";
        } finally {
            return "C";
        }
    }

    static String B() {
        String a = "a";
        try {
            a = "b";
            return a;
        } catch (Exception e) {
            a = "c";
        } finally {
            a = "d";
        }
        return a;
    }

}
