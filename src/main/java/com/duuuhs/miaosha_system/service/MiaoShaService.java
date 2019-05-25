package com.duuuhs.miaosha_system.service;

import com.duuuhs.miaosha_system.model.OrderInfo;
import com.duuuhs.miaosha_system.model.User;
import com.duuuhs.miaosha_system.vo.GoodsVo;

/**
 * @Author: DMY
 * @Date: 2019/4/19 0:01
 * @Description: 秒杀服务
 */
public interface MiaoShaService {

    /*
     * 秒杀商品
     * @parm: user
     * @parm: goods
     * return: OrderInfo
     */
    OrderInfo miaosha(User user, GoodsVo goods);



    /*
     * redis标记某件秒杀商品售完
     * @parm: goodsId
     */
    void setGoodsOver(Long goodsId);



    /*
     * redis查找某件秒杀商品是否售完
     * @parm: goodsId
     */
    boolean getGoodsOver(Long goodsId);



    /*
     * 创建秒杀地址
     * @parm: user
     * @parm: goodsId
     * @return: String
     */
    String createPath(User user, Long goodsId);



    /*
     * 验证秒杀path
     * @parm: user
     * @parm: goodsId
     * @parm: path
     * @return: boolean
     */
    boolean checkPath(User user, Long goodsId, String path);
}
