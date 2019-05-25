package com.duuuhs.miaosha_system.controller;

import com.duuuhs.miaosha_system.dao.UserMapper;
import com.duuuhs.miaosha_system.model.User;
import com.duuuhs.miaosha_system.redis.RedisService;
import com.duuuhs.miaosha_system.result.CodeMsg;
import com.duuuhs.miaosha_system.result.Reslut;
import com.duuuhs.miaosha_system.service.UserService;
import com.duuuhs.miaosha_system.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * @Author: DMY
 * @Date: 2019/4/15 13:33
 * @Description:
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    Logger logger = LoggerFactory.getLogger(LoginController.class);



    @Autowired
    private UserService userService;



    /*
     * 登录
     */
    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }


    /*
     * 登录验证
     */
    @RequestMapping("/do_login")
    @ResponseBody
    public Reslut<Boolean> doLogin(HttpServletResponse response, @RequestBody @Valid LoginVo loginVo){//使用validation校验器
        logger.info(loginVo.toString());
        userService.doLogin(response, loginVo);
        return Reslut.success(CodeMsg.SUCCESS, null);

    }

}
