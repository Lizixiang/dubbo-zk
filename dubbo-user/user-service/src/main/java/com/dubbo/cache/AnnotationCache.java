package com.dubbo.cache;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AnnotationCache {

    private final static ConcurrentHashMap<Annotation, Class<?>> classCache = new ConcurrentHashMap<Annotation, Class<?>>();

    private final static ConcurrentHashMap<Annotation, Method> methodCache = new ConcurrentHashMap<Annotation, Method>();

    @PostConstruct
    public void init() {
        // TODO: 2024/3/28 通过扫描包下的所有类，将注解修饰的类和注解放入map中
    }

}
