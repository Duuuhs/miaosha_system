package com.duuuhs.miaosha_system.vo;

import com.duuuhs.miaosha_system.model.MiaoShaGoods;
import com.duuuhs.miaosha_system.model.User;

/**
 * @Author: DMY
 * @Date: 2019/4/18 2:32
 * @Description:
 */
public class TwoTable extends MiaoShaGoods {
    private Long userId;
    private String nickName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "TwoTable{" +
                "userId=" + userId +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
