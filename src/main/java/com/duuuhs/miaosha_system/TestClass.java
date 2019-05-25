package com.duuuhs.miaosha_system;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Author: DMY
 * @Date: 2019/4/15 11:05
 * @Description:
 */
public class TestClass {

    public static void main(String[] args) {
        String str = "dumingyuan";
        String s = DigestUtils.md5Hex(str);
        System.out.println(s);
        System.out.println(s.charAt(2));

    }
}
