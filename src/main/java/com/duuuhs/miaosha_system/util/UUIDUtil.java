package com.duuuhs.miaosha_system.util;

import java.util.UUID;

/**
 * @Author: DMY
 * @Date: 2019/4/16 15:08
 * @Description:
 */
public class UUIDUtil {

    public static String uuid(){
        //去掉原生uuid的横杠,生成32位的唯一ID
        return UUID.randomUUID().toString().replace("-", "");
    }

//    public static void main(String[] args) {
//        System.out.println(uuid());
//    }
}

