package com.dubbo.core.example;

import java.util.function.LongSupplier;
import java.util.function.Supplier;

/**
 * <pre>
 *
 * </pre>
 *
 * @author lizixiang
 * @date 2023年01月10日
 */
public class SupplierExample {

    public static void main(String[] args) {
        Supplier<Integer> s1 = () -> 1;
        System.out.println(s1.get());

        LongSupplier s2 = () -> 2L;
        System.out.println(s2.getAsLong());
    }

}
