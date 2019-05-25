package com.duuuhs.miaosha_system.service;

import com.duuuhs.miaosha_system.model.User;
import com.duuuhs.miaosha_system.result.CodeMsg;
import com.duuuhs.miaosha_system.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: DMY
 * @Date: 2019/4/15 23:23
 * @Description: 用户服务
 */
public interface UserService {


    /*
     * 用户登录
     * @parm: loginVo
     * @return: Boolean
     */
    boolean doLogin(HttpServletResponse response, LoginVo loginVo);


    /*
     * 根据token从redis中取出对应的用户信息
     * @parm: token
     * @return: User
     */
    User getByToken(HttpServletResponse response, String token);


    /*
     *更新密码
     * @parm: token
     * @parm: user_id
     * @parm: fromPass
     * @return: boolean
     */
    boolean updatePassword(String token, Long user_id, String formPass);


    /*
     * 根据用户id取数据
     * @parm: user_id
     * @return: User
     */
    User getById(Long user_id);


    /*
     * 添加token到cookie中,同时更新redis
     * @parm: response
     * @parm: user
     * @return: void
     */
    void addCookie(HttpServletResponse response, String token, User user);
}
