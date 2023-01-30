package com.dubbo.core.design.liability.impl;

import com.dubbo.core.exception.Result;
import com.dubbo.core.design.liability.AbstractHandler;
import com.dubbo.core.design.liability.dto.BaseHandlerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <pre>
 *
 * </pre>
 *
 * @author lizixiang
 * @date 2023年01月17日
 */
@Component
public class SenstiveHandler extends AbstractHandler<BaseHandlerDto, Result> {

    private static final Logger logger = LoggerFactory.getLogger(SenstiveHandler.class);

    @Override
    public Result doHandle(BaseHandlerDto baseHandlerDto) {
        logger.info("SenstiveHandler...");
        return Result.SUCCESS();
    }
}
