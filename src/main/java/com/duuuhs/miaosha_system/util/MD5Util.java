package com.duuuhs.miaosha_system.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Author: DMY
 * @Date: 2019/4/15 10:45
 * @Description: MD5工具类
 */
public class MD5Util {

    //客户端设置的固定salt
    private static final String salt = "1a2b3c4d";

    /*
     * MD5加密
     * parm: value(加密字符串)
     */
    public static String md5(String value){
        return DigestUtils.md5Hex(value);
    }


    /*
     *第一次加密： 将用户输入的明文密码加上固定Salt 之后进行MD5加密，然后在网络中进行传输。当传输到达服务器端的时候，进行第二次加密。
     *   用户端： PASS = MD5(明文 + 固定Salt)
     *    parm: inputPass(用户输入密码)
     */
    public static String inputPassToFormPass(String inputPass){
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }


    /*
     *第二次加密：第一次加密后的密文和一个随机Salt结合之后，再进行一次MD5加密（这是为了防止数据库被盗，如果只进行一次MD5加密的话，可以通过反查表的方式推算出明文密码）。
     *   服务端： PASS = MD5(用户输入 + 随机Salt)
     *    parm: fromPass(第一次加密后的密文); salt(随机的salt)
     */
    public static String formPassToDBPass(String fromPass, String salt){
        String str = "" + salt.charAt(0) + salt.charAt(2) + fromPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }


    /*
     *两次加密过程
     *  parm: inputPass(用户输入的密码); saltDB(随机的salt)
     */
    public static String inputPassToDBPass(String inputPass, String saltDB){
        String fromPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(fromPass, saltDB);
        return dbPass;
    }


    public static void main(String[] args) {
        String str = "123456";//d3b1294a61a07da9b49b6e22b2cbd7f9
//        String fromPass = inputPassToFormPass(str);
        String dbPass = inputPassToDBPass(str, "abcdefg");
        System.out.println(dbPass);
    }



}
