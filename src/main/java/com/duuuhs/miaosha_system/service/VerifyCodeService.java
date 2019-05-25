package com.duuuhs.miaosha_system.service;

import com.duuuhs.miaosha_system.model.User;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @Author: DMY
 * @Date: 2019/4/25 12:02
 * @Description:  验证码服务
 */
public interface VerifyCodeService {

    /*
     * 生成验证码
     * @parm: user
     * @parm: user
     * @return: BufferedImage
     */
    BufferedImage createVerifyCode(User user, Long goodsId);


    /*
     * 生成随机的数学公式
     * @parm: rdm
     * @return: String
     */
    String generateVerifyCode(Random rdm);


    /*
     * 计算数学公式的结果
     * @parm: exp
     * @return: int
     */
    int calc(String exp);


    /*
     * 验证用户输入的验证码
     * @parm: inputVerifyCode
     * @return: boolean
     */
    boolean checkVerifyCode(User user, Long goodsId, int inputVerifyCode);

}
