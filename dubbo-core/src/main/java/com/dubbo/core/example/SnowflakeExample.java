package com.dubbo.core.example;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;

/**
 * Description:雪花算法
 *
 * @author lizixiang
 * @date 2024-04-10 16:40:27
 */
public class SnowflakeExample {

    public static void main(String[] args) {
        long workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        long l = snowflake.nextId();
    }

}
