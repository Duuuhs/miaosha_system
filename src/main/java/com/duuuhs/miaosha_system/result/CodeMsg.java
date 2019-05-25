package com.duuuhs.miaosha_system.result;

/**
 * @Author: DMY
 * @Date: 2019/4/12 12:49
 * @Description: 返回定义的正确/错误的所有异常在这里定义
 */
public class CodeMsg {

    private int code;
    private String msg;

    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVICE_ERROR = new CodeMsg(500100, "服务端异常!");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%S");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "请求非法！");
    public static CodeMsg ACCESS_LIMIT = new CodeMsg(500103, "访问过于频繁，稍后重试！");

    //登录模块异常
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500201, "手机号码不存在！");
    public static CodeMsg PSAAWORD_ERROR = new CodeMsg(500202, "密码出错！");
    public static CodeMsg SESSION_ERROR = new CodeMsg(500203, "未登录或登录失效！");


    //商品模块异常

    //订单模块异常
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500401, "订单不存在！");

    //秒杀模块异常
    public static CodeMsg MIAOSHA_OVER = new CodeMsg(500500, "商品已经秒杀完毕！");
    public static CodeMsg REPEATE_MIAOSHA = new CodeMsg(500501, "不能重复秒杀！");
    public static CodeMsg MIAOSHA_FAIL = new CodeMsg(500502, "秒杀失败！");

    //填充异常信息，用到了变态“Varargs”机制
    public CodeMsg fillArgs(Object... args){
        int code = this.code;
        String messgae = String.format(this.msg, args);
        return new CodeMsg(code, messgae);
    }

    //构造函数私有化是为了不让客户端直接调用Result类，而是通过静态方法去调用
    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //同样去掉setter方法也是为了防止客户端直接调用实例化的方法进行参数的注入
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }

}
