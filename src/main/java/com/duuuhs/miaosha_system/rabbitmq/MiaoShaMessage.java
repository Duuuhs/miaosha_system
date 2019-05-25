package com.duuuhs.miaosha_system.rabbitmq;

import com.duuuhs.miaosha_system.model.User;

/**
 * @Author: DMY
 * @Date: 2019/4/24 0:23
 * @Description:  秒杀操作携带的参数
 */
public class MiaoShaMessage {
    private User user;
    private Long goodsId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return "MiaoShaMessage{" +
                "user=" + user +
                ", goodsId=" + goodsId +
                '}';
    }
}
