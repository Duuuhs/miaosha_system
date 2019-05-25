package com.duuuhs.miaosha_system.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Author: DMY
 * @Date: 2019/4/14 16:00
 * @Description:
 */
@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;

    /*
     * 获取某个对象
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            //从redis1取出
            String str = jedis.get(realKey);
            //Json字符串转Bean
            T t = strToBean(str, clazz);
            return t;
        } finally {
            returnToPool(jedis);
        }
    }


    /*
     * 设置某个对象
     */
    public <T> Boolean set(KeyPrefix prefix, String key, T value){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //bean转json字符串
            String str = beanToStr(value);
            if (str == null){
                return false;
            }
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            //多久失效
            int seconds = prefix.expireSeconds();
            //存进redis
            if (seconds <= 0){
                jedis.set(realKey, str);
            } else {
                jedis.setex(realKey, seconds, str);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }


    /*
     * 删除某个对象
     */
    public <T> boolean delete(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            //从redis1取出
            Long del = jedis.del(realKey);
            return del > 0;
        } finally {
            returnToPool(jedis);
        }
    }
    
    
    
    /*
     * 原子递增
     */
    public <T> Long incr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }


    /*
     * 原子递减
     */
    public <T> Long decr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;
            jedis.decr(realKey);
            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }


    /*
     * 字符串转Bean对象
     */
    public static <T> T strToBean(String str, Class<T> clazz){
        if (str == null || str.length() == 0){
            return null;
        } else if (clazz == int.class || clazz == Integer.class){
            return (T)Integer.valueOf(str);
        }else if (clazz == long.class || clazz == Long.class){
            return (T)Long.valueOf(str);
        } else if (clazz == String.class){
            return (T)str;
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }


    /*
     * Bean对象转字符串
     */
    public static <T> String beanToStr(T value){
        if (value == null){
            return null;
        } else if (value.getClass() == int.class || value.getClass() == Integer.class){
            return value + "";
        } else if (value.getClass() == long.class || value.getClass() == Long.class){
            return value + "";
        } else if (value.getClass() == String.class){
            return (String)value;
        } else {
            return JSON.toJSONString(value);
        }

    }


    /*
     * 判断key是否存在
     */
    public Boolean exist(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            Boolean exists = jedis.exists(realKey);
            return exists;
        } finally {
            returnToPool(jedis);
        }
    }

    /*
     * 关闭jedis,返回连接池
     */
    private void returnToPool(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }
}
