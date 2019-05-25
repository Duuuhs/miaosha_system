package com.duuuhs.miaosha_system.controller;

import com.duuuhs.miaosha_system.model.User;
import com.duuuhs.miaosha_system.redis.RedisService;
import com.duuuhs.miaosha_system.redis.UserKey;
import com.duuuhs.miaosha_system.result.CodeMsg;
import com.duuuhs.miaosha_system.service.TestService;
import com.duuuhs.miaosha_system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.duuuhs.miaosha_system.result.Reslut;

import javax.servlet.http.HttpServletResponse;

import static com.duuuhs.miaosha_system.util.Assert.isNull;

/**
 * @Author: DMY
 * @Date: 2019/4/13 15:26
 * @Description:
 */
@Controller
@RequestMapping("/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @RequestMapping("/info")
    @ResponseBody
    public Reslut<User> getUser(HttpServletResponse response,
                                @CookieValue(value = UserKey.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                                @RequestParam(value = UserKey.COOKIE_NAME_TOKEN, required = false) String paramToken,
                                Model model){
        //根据token获取用户信息
        String token = isNull(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);
        model.addAttribute("user", user);
        return Reslut.success(CodeMsg.SUCCESS, user);
    }
}
