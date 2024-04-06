package com.dubbo.core.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description:
 *
 * @author lizixiang
 * @date 2024-04-05 14:41:54
 */
@ConfigurationProperties(prefix = "cache.propertes")
@Data
public class CacheProperties {

    // 是否允许存储空值
    private boolean allowNullValues;

    // 缓存key的前缀
    private String prefix;

    // 二级缓存过期时间
    private long l2CacheExpire;

}
