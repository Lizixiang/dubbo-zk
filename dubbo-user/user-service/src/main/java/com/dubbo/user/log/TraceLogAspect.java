package com.dubbo.user.log;


import com.dubbo.cache.TraceCache;
import com.dubbo.user.log.vo.TraceVo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(1)
public class TraceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(TraceLogAspect.class);

    @Autowired
    private TraceCache traceCache;

    @Pointcut("execution(* com.dubbo.user..*.*(..))")
    public void traceLog() {
    }

    @Around(value = "traceLog()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        TraceVo traceVo = traceCache.getTrace();
        if (traceVo == null) {
            traceVo = new TraceVo();
        }
        TraceVo newNode = traceVo.addTrace(className, methodName, parameterTypes);
        if (newNode != null) {
            traceCache.setTrace(newNode);
        }
        return pjp.proceed();
    }

}
