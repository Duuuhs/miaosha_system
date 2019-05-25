package com.duuuhs.miaosha_system.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Author: DMY
 * @Date: 2019/4/13 22:42
 * @Description:
 */
@Component
//spring boot1.5以上版本@ConfigurationProperties取消location注解后的替代方案 cannot resolve method location   与@EnableConfigurationProperties是替代关系
//没有使用@Component或@Confinguration，因此此对象不会注册到Spring容器中，需要@EnableConfigurationProperties
@PropertySource("classpath:application.properties")//使用@PropertySource来指定自定义的资源目录
@ConfigurationProperties(prefix = "spring.redis")//读取application.properties文件中以“spring.redis”开头的变量值。
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;//秒

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private int poolMaxWait;//秒;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int poolMaxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int poolMinIdle;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoolMaxWait() {
        return poolMaxWait;
    }

    public void setPoolMaxWait(int poolMaxWait) {
        this.poolMaxWait = poolMaxWait;
    }

    public int getPoolMaxIdle() {
        return poolMaxIdle;
    }

    public void setPoolMaxIdle(int poolMaxIdle) {
        this.poolMaxIdle = poolMaxIdle;
    }

    public int getPoolMinIdle() {
        return poolMinIdle;
    }

    public void setPoolMinIdle(int poolMinIdle) {
        this.poolMinIdle = poolMinIdle;
    }
}
