package com.dubbo.core.liability.impl;

import com.dubbo.core.exception.Result;
import com.dubbo.core.liability.AbstractHandler;
import com.dubbo.core.liability.dto.BaseHandlerDto;
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
public class SignHandler extends AbstractHandler<BaseHandlerDto, Result> {

    private static final Logger logger = LoggerFactory.getLogger(SignHandler.class);

    @Override
    protected Result doHandle(BaseHandlerDto baseHandlerDto) {
        logger.info("SignHandler...");
        return Result.SUCCESS();
    }
}
