package com.dubbo.cache;

import com.dubbo.user.log.vo.TraceVo;
import org.springframework.stereotype.Component;

@Component
public class TraceCache {

    private final ThreadLocal<TraceVo> cache = new ThreadLocal<>();

    public TraceVo getTrace() {
        return cache.get();
    }

    public void setTrace(TraceVo traceVo) {
        cache.set(traceVo);
    }

}
