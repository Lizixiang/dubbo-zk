package com.dubbo.core.cache;

import com.dubbo.core.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:自动装配CompositeCacheManager
 *
 * @author lizixiang
 * @date 2024-04-05 14:33:56
 */
@Configuration
@AutoConfigureAfter(RedisUtils.class)
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(CacheAutoConfiguration.class);

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private CacheProperties cacheProperties;

    @Bean("l2CacheManager")
    public CompositeCacheManager l2CacheManager() {
        logger.info("l2Cache init success...");
        return new CompositeCacheManager(cacheProperties, redisUtils);
    }

}
