package com.duuuhs.miaosha_system.redis;

/**
 * @Author: DMY
 * @Date: 2019/4/14 22:53
 * @Description:
 */
public class OrderKey extends KeyPrefixImpl{





    private OrderKey(String prefix) {
        super(prefix);
    }

    private OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey getOrderByUserIdGoodsId = new OrderKey(0, "uigi");

}
