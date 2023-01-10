package com.dubbo.core.filter;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * <pre>
 * 此处填写中文类名
 * 此处填写中文类描述
 * </pre>
 *
 * @author lizixiang
 * @date 2023年01月09日
 */
@Component
public class RiskFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RiskFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("riskFilter入参={}", JSON.toJSONString(request.getParameterMap()));
        chain.doFilter(request, response);
    }
}
