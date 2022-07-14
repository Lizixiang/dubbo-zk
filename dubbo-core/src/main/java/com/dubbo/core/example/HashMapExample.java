package com.dubbo.core.example;

import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class HashMapExample {

    public static HashMapExample a = new HashMapExample();

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        map.put("1", "1");
        map.put("2", "1");
        map.put("3", "1");
        map.put("4", "1");
        map.put("5", "1");
        map.put("6", "1");
        map.put("7", "1");
        map.put("8", "1");
        map.put("9", "1");
        map.put("10", "1");
        map.put("11", "1");
        map.put("12", "1");
        map.put("13", "1");
        map.put("13", "1");
        map.get("13");

        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("1", "1");

        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("1", "1");
        concurrentHashMap.get("1");
        concurrentHashMap.remove("1");
        concurrentHashMap.size();
        synchronized ("1") {

        }
    }

}
