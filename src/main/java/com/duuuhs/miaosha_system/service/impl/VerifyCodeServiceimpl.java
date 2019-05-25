package com.duuuhs.miaosha_system.service.impl;

import com.duuuhs.miaosha_system.model.User;
import com.duuuhs.miaosha_system.redis.GoodsKey;
import com.duuuhs.miaosha_system.redis.RedisService;
import com.duuuhs.miaosha_system.service.VerifyCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import static com.duuuhs.miaosha_system.util.Assert.isNull;

/**
 * @Author: DMY
 * @Date: 2019/4/25 12:02
 * @Description:
 */
@Service
public class VerifyCodeServiceimpl implements VerifyCodeService {


    @Autowired
    private RedisService redisService;


    Logger logger = LoggerFactory.getLogger(VerifyCodeService.class);

    public static final char[] ops = new char[] {'+', '-', '*'};


    /*
     * 生成验证码
     * @parm: user
     * @parm: user
     * @return: BufferedImage
     */
    public BufferedImage createVerifyCode(User user, Long goodsId){
        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(GoodsKey.getMiaoshaVerifyCode, user.getUserId()+"_"+goodsId, rnd);
        //输出图片
        return image;
    }


    /*
     * 生成随机的数学公式
     * @parm: rdm
     * @return: String
     */
    public String generateVerifyCode(Random rdm){
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }


    /*
     * 计算数学公式的结果
     * @parm: exp
     * @return: int
     */
    public int calc(String exp){
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /*
     * 验证用户输入的验证码
     * @parm: inputVerifyCode
     * @return: boolean
     */
    public boolean checkVerifyCode(User user, Long goodsId, int inputVerifyCode){
        if (isNull(inputVerifyCode)){
            return false;
        }
        logger.info("用户输入验证码为:" + inputVerifyCode);
        int verifyCode = redisService.get(GoodsKey.getMiaoshaVerifyCode, user.getUserId() + "_" + goodsId, Integer.class);
        logger.info("系统验证码为:" + inputVerifyCode);
        //每个验证码只校验一次
//        redisService.delete(GoodsKey.getMiaoshaVerifyCode, user.getUserId() + "_" + goodsId);
        return inputVerifyCode == verifyCode;
    }
}
