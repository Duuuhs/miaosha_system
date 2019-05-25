package com.duuuhs.miaosha_system.redis;

/**
 * @Author: DMY
 * @Date: 2019/4/14 16:56
 * @Description:  获取类的前缀
 */
public interface KeyPrefix {

    /*
     * 设置失效时间，0代表永不过期
     */
    int expireSeconds();

    /*
     * 拼接成"类名+属性名"
     */
    String getPrefix();
}
