package com.duuuhs.miaosha_system.access;

import com.duuuhs.miaosha_system.model.User;

/**
 * @Author: DMY
 * @Date: 2019/4/25 17:24
 * @Description:  利用ThreadLocal存放用户的信息
 */
public class UserContext {
    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }

}
