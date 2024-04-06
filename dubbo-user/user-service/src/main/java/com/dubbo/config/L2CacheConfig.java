package com.dubbo.config;

import com.dubbo.core.cache.CaffeinePropsHolder;
import com.dubbo.user.enums.CacheManagerEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Description:二级缓存配置类
 *
 * @author lizixiang
 * @date 2024-04-05 15:58:03
 */
@Component
public class L2CacheConfig implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        CaffeinePropsHolder.register(CacheManagerEnum.USER.getKey(), CacheManagerEnum.USER.getInitialCapacity(),
                CacheManagerEnum.USER.getMaximumSize(), CacheManagerEnum.USER.getExpireAfterWrite(),
                CacheManagerEnum.USER.getExpireAfterAccess(), CacheManagerEnum.USER.getRefreshAfterWrite(), CacheManagerEnum.USER.getL2CacheExpire());
    }

}
