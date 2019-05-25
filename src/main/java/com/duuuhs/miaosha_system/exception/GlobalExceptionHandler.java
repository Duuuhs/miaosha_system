package com.duuuhs.miaosha_system.exception;

import com.duuuhs.miaosha_system.result.CodeMsg;
import com.duuuhs.miaosha_system.result.Reslut;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @Author: DMY
 * @Date: 2019/4/16 0:51
 * @Description: 自定义全局异常处理器,特殊的控制器,出现异常时调用
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)//value代表拦截哪种异常
    public Reslut<String> exceptionHandler(HttpServletRequest request,Exception e){
        e.printStackTrace();
        if (e instanceof GlobalException){//全局异常信息
            GlobalException ex = (GlobalException)e;
            CodeMsg codeMsg = ex.getCodeMsg();
            return Reslut.error(codeMsg);
        } else if (e instanceof BindException){//绑定信息异常
            BindException ex = (BindException)e;
            List<ObjectError> allErrors = ex.getAllErrors();
            ObjectError objectError = allErrors.get(0);//默认取第一条
            String msg = objectError.getDefaultMessage();//获取错误信息
            return Reslut.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        } else {//其他异常信息
            return Reslut.error(CodeMsg.SERVICE_ERROR);
        }
    }
}
