package com.duuuhs.miaosha_system.redis;

/**
 * @Author: DMY
 * @Date: 2019/4/14 16:58
 * @Description: 获取类的前缀
 */
public class KeyPrefixImpl implements KeyPrefix{

    private int expireSeconds;//过期时间

    private String prefix;


    public KeyPrefixImpl(String prefix) {//0代表永不过期
        this(0, prefix);
    }

    public KeyPrefixImpl(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    /*
     * 设置失效时间，0代表永不过期
     */
    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    /*
     * 拼接成"类名+属性名"
     */
    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + "_" + prefix;
    }


}
