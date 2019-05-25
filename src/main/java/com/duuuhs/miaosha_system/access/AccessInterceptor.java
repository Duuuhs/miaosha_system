package com.duuuhs.miaosha_system.access;

import com.alibaba.fastjson.JSON;
import com.duuuhs.miaosha_system.model.User;
import com.duuuhs.miaosha_system.redis.RedisService;
import com.duuuhs.miaosha_system.redis.UserKey;
import com.duuuhs.miaosha_system.result.CodeMsg;
import com.duuuhs.miaosha_system.result.Reslut;
import com.duuuhs.miaosha_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @Author: DMY
 * @Date: 2019/4/25 16:35
 * @Description:
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            //取cookie用户
            User user = getUser(request, response);
            UserContext.setUser(user);

            HandlerMethod handlerMethod = (HandlerMethod)handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null){
                return true;
            }
            String key = request.getRequestURI();
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            //用户登录
            if (needLogin){
                if (user == null){
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
                key += "_" + user.getUserId();
            } else {
                //do nothing
            }
            //用户访问限制
            Integer count = redisService.get(UserKey.withExpire(seconds), key, Integer.class);
            if (count == null){
                redisService.set(UserKey.withExpire(seconds), key, 1);
            } else if (count < maxCount){
                redisService.incr(UserKey.withExpire(seconds), key);
            } else {
                render(response, CodeMsg.ACCESS_LIMIT);
                return false;
            }
        }
        return true;
    }

    /*
     * 获取用户信息
     * @parm: request
     * @parm: response
     * @return: User
     */
    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(UserKey.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, UserKey.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return userService.getByToken(response, token);
    }

    /*
     * 根据name获取某个cookie
     * @parm: request
     * @parm: cookiName
     * @return: String
     */
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[]  cookies = request.getCookies();
        if(cookies == null || cookies.length <= 0){
            return null;
        }
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }


    private void render(HttpServletResponse response, CodeMsg cm)throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str  = JSON.toJSONString(Reslut.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
