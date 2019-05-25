package com.duuuhs.miaosha_system.exception;

import com.duuuhs.miaosha_system.result.CodeMsg;
import com.duuuhs.miaosha_system.result.Reslut;

/**
 * @Author: DMY
 * @Date: 2019/4/16 14:10
 * @Description: 自定义全局异常,接收到异常会传给GlobalExceptionHandler进行处理
 */
public class GlobalException extends RuntimeException {

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg){
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
