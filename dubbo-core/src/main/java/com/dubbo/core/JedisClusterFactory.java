package com.dubbo.core;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lizixiang
 * @since 2022/4/7
 */
@Configuration
public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean {

    private JedisCluster jedisCluster;
    @Value("${spring.redis.cluster.nodes}")
    private String nodes;
    private int connectionTimeout = 2000;
    private int soTimeout = 3000;
    private int maxRedirections = 5;

    public JedisClusterFactory() {
    }

    @Override
    public JedisCluster getObject() throws Exception {
        return this.jedisCluster;
    }

    @Override
    public Class<?> getObjectType() {
        return this.jedisCluster != null ? this.jedisCluster.getClass() : JedisCluster.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<HostAndPort> hostAndPorts = new HashSet<>();
        String[] n1 = this.nodes.split(",");
        for (String node : n1) {
            String[] split = node.split(":");
            hostAndPorts.add(new HostAndPort(split[0], Integer.parseInt(split[1])));
        }
        jedisCluster = new JedisCluster(hostAndPorts, this.connectionTimeout, this.soTimeout, this.maxRedirections, new GenericObjectPoolConfig<>());
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getMaxRedirections() {
        return maxRedirections;
    }

    public void setMaxRedirections(int maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    public String set(String key, String value) {
        String r = jedisCluster.set(key, value);
        return r;
    }

    public Object eval(String script) {
        Object r = jedisCluster.eval(script);
        return r;
    }

    public Object eval(String script, int keyCount, String... params) {
        Object r = jedisCluster.eval(script, keyCount, params);
        return r;
    }

    public Object eval(String script, List<String> keys, List<String> args) {
        Object r = jedisCluster.eval(script, keys, args);
        return r;
    }

    public Object evalSha(String script, List<String> keys, List<String> args) {
        Object r = jedisCluster.evalsha(script, keys, args);
        return r;
    }

    public long incr(String key) {
        long r = jedisCluster.incr(key);
        return r;
    }

}
