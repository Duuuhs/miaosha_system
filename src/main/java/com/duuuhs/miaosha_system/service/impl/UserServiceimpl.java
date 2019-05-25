package com.duuuhs.miaosha_system.service.impl;

import com.duuuhs.miaosha_system.controller.LoginController;
import com.duuuhs.miaosha_system.dao.UserMapper;
import com.duuuhs.miaosha_system.exception.GlobalException;
import com.duuuhs.miaosha_system.model.User;
import com.duuuhs.miaosha_system.redis.RedisService;
import com.duuuhs.miaosha_system.redis.UserKey;
import com.duuuhs.miaosha_system.result.CodeMsg;
import com.duuuhs.miaosha_system.service.UserService;
import com.duuuhs.miaosha_system.util.MD5Util;
import com.duuuhs.miaosha_system.util.UUIDUtil;
import com.duuuhs.miaosha_system.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static com.duuuhs.miaosha_system.util.Assert.isNull;
import static com.duuuhs.miaosha_system.util.Assert.notNull;

/**
 * @Author: DMY
 * @Date: 2019/4/15 23:25
 * @Description: 用户服务实现类
 */
@Service
public class UserServiceimpl implements UserService {


    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    /*
     * 用户登录
     * parm: loginVo
     * return: Boolean
     */
    @Override
    public boolean doLogin(HttpServletResponse response, LoginVo loginVo) {
        if (isNull(loginVo)){
            throw new GlobalException(CodeMsg.SERVICE_ERROR);
        }
        //用户是否存在
        String mobile = loginVo.getMobile();
        User user = userMapper.selectByPrimaryKey(Long.parseLong(mobile));
        if (isNull(user)){
            //用户不存在
            logger.info(CodeMsg.MOBILE_NOT_EXIST.getMsg());
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String fromPass = loginVo.getPassword();
        String dbSalt = user.getSalt();
        String dbPass = user.getPassword();
        String entryPass = MD5Util.formPassToDBPass(fromPass, dbSalt);
        if (!dbPass.equals(entryPass)){
            throw new GlobalException(CodeMsg.PSAAWORD_ERROR);
        }

        //生成全局唯一的token
        String token = UUIDUtil.uuid();
        //生成唯一token并添加到cookie中,同时更新redis
        addCookie(response, token, user);
        return true;
    }


    /*
     * 根据token从redis中取出对应的用户信息
     * parm: token
     * return: User
     */
    @Override
    public User getByToken(HttpServletResponse response, String token) {
        if (isNull(token)){
            return null;
        }
        User user = redisService.get(UserKey.token, token, User.class);
        if (notNull(user)){
            //延长有效期
            addCookie(response, token, user);
        }
        return user;
    }


    /*
     *更新密码
     * @parm: token
     * @parm: user_id
     * @parm: fromPass
     * @return: boolean
     */
    @Override
    @Transactional
    public boolean updatePassword(String token, Long user_id, String formPass){
        if (isNull(token) || isNull(user_id) ||isNull(formPass)){
            return false;
        }
        //取user
        User user = getById(user_id);
        if (isNull(user)){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        user.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        userMapper.updateByPrimaryKey(user);
        //缓存更新 token,user
        redisService.delete(UserKey.getById, ""+user_id);
        redisService.set(UserKey.token, token, user);
        return true;

    }


    /*
     * 根据用户id取数据
     * @parm: user_id
     * @return: User
     */
    public User getById(Long user_id){
        if (isNull(user_id)){
            return null;
        }
        //取缓存
        User user = redisService.get(UserKey.getById, "" + user_id, User.class);
        if (notNull(user)){
            return user;
        }
        //数据库获取user
        return userMapper.selectByPrimaryKey(user_id);
    }

    /*
     * 添加token到cookie中,同时更新redis
     * parm: response
     * parm: user
     * return: void
     */
    @Override
    public void addCookie(HttpServletResponse response, String token, User user) {

        //缓存到redis中
        redisService.set(UserKey.token, token, user);
        //将用户UUID放进客户端cookie中
        Cookie cookie = new Cookie(UserKey.COOKIE_NAME_TOKEN, token);
        //cookie有效期与redis有效期保持一致
        cookie.setMaxAge(UserKey.token.expireSeconds());
        //默认缓存在网站的根目录下
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
