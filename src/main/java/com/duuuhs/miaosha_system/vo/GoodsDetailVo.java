package com.duuuhs.miaosha_system.vo;

import com.duuuhs.miaosha_system.model.User;

/**
 * @Author: DMY
 * @Date: 2019/4/22 13:44
 * @Description:
 */
public class GoodsDetailVo {
    private User user;              //用户信息
    private int miaoshaStatus;      //秒杀状态
    private int remainSeconds;      //秒杀倒计时(秒)
    private GoodsVo goods;        //商品信息

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoodsVo(GoodsVo goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return "GoodsDetailVo{" +
                "user=" + user +
                ", miaoshaStatus=" + miaoshaStatus +
                ", remainSeconds=" + remainSeconds +
                ", goods=" + goods +
                '}';
    }
}
