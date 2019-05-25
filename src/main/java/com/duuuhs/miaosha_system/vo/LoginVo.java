package com.duuuhs.miaosha_system.vo;

import com.duuuhs.miaosha_system.validator.IsMobile;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @Author: DMY
 * @Date: 2019/4/15 17:11
 * @Description: 登录的接收bean
 */
public class LoginVo {

    @NotNull
    @IsMobile//自定义了一个校验器
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
