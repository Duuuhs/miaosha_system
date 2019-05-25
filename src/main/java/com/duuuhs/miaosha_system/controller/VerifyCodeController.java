package com.duuuhs.miaosha_system.controller;

import com.duuuhs.miaosha_system.model.User;
import com.duuuhs.miaosha_system.redis.UserKey;
import com.duuuhs.miaosha_system.result.CodeMsg;
import com.duuuhs.miaosha_system.result.Reslut;
import com.duuuhs.miaosha_system.service.UserService;
import com.duuuhs.miaosha_system.service.VerifyCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

import static com.duuuhs.miaosha_system.util.Assert.isNull;

/**
 * @Author: DMY
 * @Date: 2019/4/25 11:53
 * @Description:   验证码控制器
 */
@Controller
public class VerifyCodeController {

    Logger logger = LoggerFactory.getLogger(MiaoShaController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private VerifyCodeService verifyCodeService;


    @RequestMapping(value = "verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Reslut<Integer> getVerifyCode(HttpServletResponse response,
                                     @CookieValue(value = UserKey.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                                     @RequestParam(value = UserKey.COOKIE_NAME_TOKEN, required = false) String paramToken,
                                     @RequestParam("goodsId")Long goodsId) {
        //根据token获取用户信息
        String token = isNull(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);
        if (isNull(user)) {
            logger.info(CodeMsg.SESSION_ERROR.getMsg());
            return Reslut.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image = verifyCodeService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Reslut.error(CodeMsg.MIAOSHA_FAIL);
        }
    }
}
