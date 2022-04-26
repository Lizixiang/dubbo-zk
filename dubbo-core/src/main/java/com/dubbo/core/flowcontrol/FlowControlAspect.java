package com.dubbo.core.flowcontrol;

import com.dubbo.core.flowcontrol.enums.AlgorithmEnum;
import com.dubbo.core.flowcontrol.enums.FlowControlEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author lizixiang
 * @since 2022/4/8
 */
@Aspect
@Component
public class FlowControlAspect {

    private static final Logger logger = LoggerFactory.getLogger(FlowControlAspect.class);

    @Pointcut("@annotation(com.dubbo.core.flowcontrol.FlowControl)")
    public void flowControl() {
    }

    @Before(value = "flowControl()")
    public void before(JoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        FlowControl flowControl = method.getAnnotation(FlowControl.class);
        String key = flowControl.key();
        AlgorithmEnum algorithm = flowControl.algorithm();
        FlowControlEnum[] enums = flowControl.method();
        long duration = flowControl.duration();
        int flowRate = flowControl.flowRate();
        int max = flowControl.max();
        for (FlowControlEnum controlEnum :enums){
            FlowControlStrategy strategy = FlowControlStrategyFactory.getFlowControlStrategy(algorithm);
            FlowControlDto dto = FlowControlDto.builder().key(key).algorithm(algorithm).method(controlEnum).duration(duration).flowRate(flowRate).max(max).build();
            switch (controlEnum) {
                case KEY:
                    if (pjp.getArgs() != null && pjp.getArgs().length > 0) {
                        StandardEvaluationContext context = new StandardEvaluationContext();
                        LocalVariableTableParameterNameDiscoverer discover = new LocalVariableTableParameterNameDiscoverer();
                        String[] parameterNames = discover.getParameterNames(method);
                        Object[] args = pjp.getArgs();
                        for (int i = 0; i < parameterNames.length; i++) {
                            context.setVariable(parameterNames[i], args[i]);
                        }
                        SpelExpressionParser parser = new SpelExpressionParser();
                        String parseKey = parser.parseExpression(key).getValue(context, String.class);
                        dto.setKey(parseKey);
                    }
                    break;
                case IP:

                    break;
            }
            strategy.flowControl(dto);
        }
    }

}
