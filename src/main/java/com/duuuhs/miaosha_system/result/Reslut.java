package com.duuuhs.miaosha_system.result;

import com.duuuhs.miaosha_system.result.CodeMsg;

/**
 * @Author: DMY
 * @Date: 2019/4/12 0:21
 * @Description: 控制器返回的实体类
 */
public class Reslut<T> {

    private int code;
    private String msg;
    private T data;

    /*
     构造函数私有化是为了不让客户端直接调用Result类，而是通过success或者error去调用
     */
    private Reslut(CodeMsg codeMsg) {
        if (codeMsg == null){
            return;
        } else {
          this.code = codeMsg.getCode();
          this.msg = codeMsg.getMsg();
        }
    }
    private Reslut(CodeMsg codeMsg, T data) {
        if (codeMsg == null){
            return;
        } else {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
            this.data = data;
        }
    }

    //成功的时候调用这个方法
    public static <T> Reslut<T> success(CodeMsg codeMsg, T data){
        return new Reslut<T>(codeMsg, data);
    }

    //错误的时候调用这个方法
    public static <T>Reslut <T> error(CodeMsg codeMsg){
        return new Reslut<T>(codeMsg);
    }

    //同样去掉setter方法也是为了防止客户端直接调用实例化的方法进行参数的注入
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }



    @Override
    public String toString() {
        return "Reslut{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }


}
