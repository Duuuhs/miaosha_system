package com.duuuhs.miaosha_system.service;

import com.duuuhs.miaosha_system.model.*;
import com.duuuhs.miaosha_system.vo.GoodsVo;

/**
 * @Author: DMY
 * @Date: 2019/4/18 23:46
 * @Description: 订单服务
 */
public interface OrderService {

    /*
     * 根据订单号id查找订单
     * @parm: orderId
     * @return: OrderInfo
     */
    OrderInfo getOrderById(long orderId);



    /*
     * 根据用户id与秒杀商品id查找订单信息
     * @parm: userId
     * @parm: goodsId
     * @return: MiaoShaGoods
     */
    MiaoShaOrder getMiaoShaOrderByUserIdGoodsId(Long userId, Long goodsId);

    /*
     * 下订单order_info,miaosha_order
     * @parm: user
     * @parm: goods
     * @return OrderInfo
     */
    OrderInfo createOrder(User user, GoodsVo goods);


    /*
     * 获取秒杀结果 orderId:秒杀成功;-1:秒杀失败;0:排队中
     * @parm: userId
     * @parm: goodsId
     * @return: Long
     */
    Long getMiaoShaResult(Long userId, Long goodsId);
}
