package com.duuuhs.miaosha_system.config;

import com.duuuhs.miaosha_system.model.User;
import com.duuuhs.miaosha_system.redis.RedisService;
import com.duuuhs.miaosha_system.redis.UserKey;
import com.duuuhs.miaosha_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.duuuhs.miaosha_system.util.Assert.isNull;
/**
 * @Author: DMY
 * @Date: 2019/4/17 1:01
 * @Description: 验证是否登录时,要查找cookie或者参数中是否带着cookie的字段,该类用于解析cookie的User对象
 */
@Service
public class UserArgumentResolvers implements HandlerMethodArgumentResolver {

    @Autowired
    private UserService userService;


    /*
     * User类做参数校验
     * @parm: methodParameter
     * @return: boolean
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParameterType();//如若不是User类不进行接下来操作
        return parameterType == User.class;
    }

    /*
     * supportsParameter()方法为false则执行该方法
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeResponse(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String parmToken = request.getParameter(UserKey.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, UserKey.COOKIE_NAME_TOKEN);
        if (isNull(parmToken) && isNull(cookieToken)){
            return null;
        }
        String token = isNull(parmToken) ? cookieToken : parmToken;
        User user = userService.getByToken(response, token);
        return user;
    }


    /*
     * 查找某个cookie对应的value
     * parm: request
     * parm: cookieName
     * return: String
     */
    public String getCookieValue(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals(cookieName)){
                return c.getValue();
            }
        }
        return null;


    }
}
