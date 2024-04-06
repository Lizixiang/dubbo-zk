package com.dubbo.core.util;

import com.dubbo.core.JedisClusterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lizixiang
 * @since 2022/4/8
 */
@Component
public class RedisUtils {

    @Autowired
    private JedisClusterFactory jedis;

    public long expire(String key, long seconds) {
        return jedis.expire(key, seconds);
    }

    public long expire(byte[] key, long seconds) {
        return jedis.expire(key, seconds);
    }

    public String set(String key, String value) {
        String r = jedis.set(key, value);
        return r;
    }

    public String set(byte[] key, byte[] value) {
        String r = jedis.set(key, value);
        return r;
    }

    public String get(String key) {
        String r = jedis.get(key);
        return r;
    }

    public byte[] get(byte[] key) {
        return jedis.get(key);
    }

    public long del(String key) {
        return jedis.del(key);
    }

    public Object eval(String script) {
        Object r = jedis.eval(script);
        return r;
    }

    public Object eval(String script, int keyCount, String... params) {
        Object r = jedis.eval(script, keyCount, params);
        return r;
    }

    public Object eval(String script, List<String> keys, List<String> args) {
        Object r = jedis.eval(script, keys, args);
        return r;
    }

    public Object evalSha(String script, List<String> keys, List<String> args) {
        Object r = jedis.evalSha(script, keys, args);
        return r;
    }

    public long incr(String key) {
        long r = jedis.incr(key);
        return r;
    }

}
