package com.duuuhs.miaosha_system.redis;

/**
 * @Author: DMY
 * @Date: 2019/4/14 22:53
 * @Description:
 */
public class UserKey extends KeyPrefixImpl{

    //token有效期
    public static final int TOKEM_EXPIRE = 3600 * 24 * 2;
    //用户在客户端cookie的名称
    public static final String COOKIE_NAME_TOKEN = "cookie_usertoken";

    private UserKey(String prefix) {
        super(prefix);
    }

    private UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }


    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey(0,"name");
    //用户登录后会生成一个token
    public static UserKey token = new UserKey(TOKEM_EXPIRE,"usertoken");
    //用户定时访问某个服务的限制
    public static UserKey withExpire(int expireSeconds){
        return  new UserKey(expireSeconds, "access");
    }

}
