package com.dubbo.user.util;

import com.dubbo.core.util.SpringContextUtils;
import com.dubbo.cache.TraceCache;
import com.dubbo.user.log.vo.TraceVo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtils {

    private static TraceCache traceCache;

    static {
        traceCache = SpringContextUtils.getBean(TraceCache.class);
    }

    /**
     * 根据当前方法名向上追溯父调用获取指定注解
     * 追溯：当前方法名->当前类->父类调用方法->父类调用类->递归追溯
     *
     * @param clazz           类
     * @param methodName      方法名
     * @param annotationClass 注解
     * @return 如果同时存在，返回优先级：方法>类名>父类
     */
    public static Annotation getAnnotation(Class<?> clazz, String methodName, Class<? extends Annotation> annotationClass) throws ClassNotFoundException {
        try {
            TraceVo trace = traceCache.getTrace();
            if (trace == null) return null;
            // 根据类名和方法名获取trace
            TraceVo currentTrace = trace.getTrace(clazz.getName(), methodName);
            if (currentTrace == null) return null;
            Method method = clazz.getDeclaredMethod(methodName, currentTrace.getParameterTypes());
            if (method.isAnnotationPresent(annotationClass)) {
                return method.getAnnotation(annotationClass);
            }
            if (clazz.isAnnotationPresent(annotationClass)) {
                return clazz.getAnnotation(annotationClass);
            }
            List<StackTraceElement> elements = Arrays.asList(Thread.currentThread().getStackTrace())
                    .stream().filter(e -> e.getClassName().contains("com.dubbo")).collect(Collectors.toList());
            for (StackTraceElement element : elements) {
                Class<?> eClazz = Class.forName(element.getClassName());
                TraceVo t = trace.getTrace(eClazz.getName(), element.getMethodName());
                if (t == null) continue;
                Method eMethod = eClazz.getDeclaredMethod(element.getMethodName(), t.getParameterTypes());
                if (eMethod.isAnnotationPresent(annotationClass)) {
                    return eMethod.getAnnotation(annotationClass);
                }
                if (eClazz.isAnnotationPresent(annotationClass)) {
                    return eClazz.getAnnotation(annotationClass);
                }
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
