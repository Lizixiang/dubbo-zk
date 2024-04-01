package com.dubbo.user.log.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors
public class TraceVo {

    private String traceId;

    private String className;

    private String methodName;

    private Class<?>[] parameterTypes;

    private TraceVo previous;

    private TraceVo next;

    /**
     * 向当前trace添加子节点
     *
     * @param c
     * @param m
     * @param p
     * @return 返回最后一个子节点
     */
    public TraceVo addTrace(String c, String m, Class<?>[] p) {
        // 如果traceId为空代表是第一个节点元素
        if (StringUtils.isBlank(this.traceId)) {
            String uuid = UUID.randomUUID().toString();
            this.traceId = uuid;
            this.className = c;
            this.methodName = m;
            this.parameterTypes = p;
        } else if (this.getNext() == null) {
            // TODO: 2024/3/29  排查使用this会报栈溢出
            TraceVo nextNode = new TraceVo(this.traceId, c, m, p, this, null);
            this.setNext(nextNode);
        } else {
            // 如果不是首个节点，也不是最后一个节点，直接返回null
            return null;
        }
        return Optional.ofNullable(this.getNext()).orElse(this);
    }

    /**
     * 根据类名和方法名获取trace
     *
     * @param c
     * @param m
     * @return
     */
    public TraceVo getTrace(String c, String m) {
        if (this.className.equals(c) && this.methodName.equals(m)) {
            return this;
        }
        return getTrace(this.previous, c, m);
    }

    private TraceVo getTrace(TraceVo traceVo, String c, String m) {
        if (traceVo == null) {
            return null;
        }
        if (traceVo.getClassName().equals(c) && traceVo.getMethodName().equals(m)) {
            return traceVo;
        }
        if (traceVo.getPrevious() == null) {
            return null;
        }
        return getTrace(traceVo.getPrevious(), c, m);
    }

    @Override
    public String toString() {
        /**
         * toString方法不打印节点信息，否则会递归循环调用自身对象，导致堆栈溢出
         */
        return "TraceVo{" +
                "traceId='" + traceId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                '}';
    }
}
