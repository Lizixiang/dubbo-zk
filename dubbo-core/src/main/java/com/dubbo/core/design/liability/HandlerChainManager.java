package com.dubbo.core.design.liability;

import com.dubbo.core.design.liability.dto.BaseHandlerDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *
 * </pre>
 *
 * @author lizixiang
 * @date 2023年01月17日
 */
@Component
public class HandlerChainManager {

    @Resource
    private Map<String, IHandler> handlerMap;

    public <T extends BaseHandlerDto, R> R executeHandle(T t) {
        R r = null;
        List<AbstractHandler> handlerChain = getHandlerChain(t.getClassNames());

        if (handlerChain.isEmpty()) return r;

        for (AbstractHandler handler : handlerChain) {
            r = (R) handler.handle(t);
        }
        return r;
    }

    public List<AbstractHandler> getHandlerChain(List<String> types) {
        List<AbstractHandler> handlerChain = new ArrayList<>();
        if (!CollectionUtils.isEmpty(types)) {
            types.stream().forEach(e -> {
                if (handlerMap.containsKey(e)) {
                    handlerChain.add((AbstractHandler) handlerMap.get(e));
                }
            });
        }
        return handlerChain;
    }

}
