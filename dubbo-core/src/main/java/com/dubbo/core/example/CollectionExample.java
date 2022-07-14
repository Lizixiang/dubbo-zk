package com.dubbo.core.example;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lizixiang
 * @since 2022/1/11
 */
public class CollectionExample {

    public static void main(String[] args) {
//        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4);
//        List<Integer> integers = list.subList(2, list.size() + 1);
//        System.out.println(integers);

        ArrayList<Integer> list1 = Lists.newArrayList(22, 1, 2, 25, -1,18, 19);
        ArrayList<Integer> list2 = Lists.newArrayList(1, 2, 18, 25, 19, 22);
        List<Integer> collect = list1.stream().sorted((v1, v2) -> {
            return list2.indexOf(v1) > list2.indexOf(v2) ? 1 : -1;
        }).collect(Collectors.toList());
        System.out.println(collect);

        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("1", "1");
        hashtable.contains("1");

        HashMap<String, String> map = new HashMap<>();
        map.containsValue("1");

        long l2 = System.currentTimeMillis();
        LinkedList<String> linkedList = new LinkedList<>();
        for (int i = 0; i <2000; i++) {
            linkedList.add(i+"");
        }
        long l3 = System.currentTimeMillis();
        System.out.println(l3 - l2);

        long l = System.currentTimeMillis();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <2000; i++) {
            list.add(i+"");
        }
        long l1 = System.currentTimeMillis();
        System.out.println(l1 - l);

        Vector<String> vector = new Vector<>();
        vector.add("1");
        vector.add("1");
        vector.add("1");
        vector.add("1");
        vector.add("1");
        vector.add("1");
        vector.add("1");
        vector.add("1");
        vector.add("1");
        vector.add("1");
        vector.add("1");
        System.out.println(vector.capacity());

        ArrayList<String> list3 = new ArrayList<>();
        list3.add("1");
        list3.add("1");
        list3.add("1");
        list3.add("1");
        list3.add("1");
        list3.add("1");
        list3.add("1");
        list3.add("1");
        list3.add("1");
        list3.add("1");
        list3.add("1");
        System.out.println(list3);

        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("1");
        hashSet.contains("1");
    }

}
