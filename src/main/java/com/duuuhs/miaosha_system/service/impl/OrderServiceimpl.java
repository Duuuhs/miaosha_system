package com.duuuhs.miaosha_system.service.impl;

import com.duuuhs.miaosha_system.dao.MiaoShaOrderMapper;
import com.duuuhs.miaosha_system.dao.OrderInfoMapper;
import com.duuuhs.miaosha_system.model.*;
import com.duuuhs.miaosha_system.service.MiaoShaService;
import com.duuuhs.miaosha_system.service.OrderService;
import com.duuuhs.miaosha_system.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import static com.duuuhs.miaosha_system.util.Assert.notNull;
/**
 * @Author: DMY
 * @Date: 2019/4/18 23:46
 * @Description:
 */
@Service
public class OrderServiceimpl implements OrderService {

    @Autowired
    private MiaoShaOrderMapper miaoShaOrderMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private MiaoShaService miaoShaService;
    

    Logger logger = LoggerFactory.getLogger(OrderServiceimpl.class);


    /*
     * 根据订单号id查找订单
     * @parm: orderId
     * @return: OrderInfo
     */
    public OrderInfo getOrderById(long orderId){
        return orderInfoMapper.selectByPrimaryKey(orderId);
    }



    /*
     * 根据用户id与秒杀商品id查找订单信息
     * @parm: userId
     * @parm: goodsId
     * @return: MiaoShaGoods
     */
    @Override
    public MiaoShaOrder getMiaoShaOrderByUserIdGoodsId(Long userId, Long goodsId){
        MiaoShaOrder miaoShaOrder = miaoShaOrderMapper.getMiaoShaOrderByUserIdGoodsId(userId, goodsId);
        return miaoShaOrder;
    }

    /*
     * 下订单order_info,miaosha_order
     * @parm: user
     * @parm: goods
     * @return OrderInfo
     */
    @Override
    @Transactional
    public OrderInfo createOrder(User user, GoodsVo goods){
        //下单order_info
        Date date = new Date();
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setCreateDate(date);
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsName(goods.getName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setUserId(user.getUserId());
        orderInfo.setStatus(0);
        long lastInsert_id = orderInfoMapper.insertGetLastInsertKey(orderInfo);
        logger.info("插入的orderinfo数据条的id为:" + orderInfo.getId());
        //下单miaosha_order
        MiaoShaOrder miaoShaOrder = new MiaoShaOrder();
        miaoShaOrder.setGoodsId(goods.getId());
        miaoShaOrder.setUserId(user.getUserId());
        miaoShaOrder.setOrderId(orderInfo.getId());
        miaoShaOrderMapper.insert(miaoShaOrder);
        return orderInfo;

    }

    /*
     * 获取秒杀结果 orderId:秒杀成功;-1:秒杀失败;0:排队中
     * @parm: userId
     * @parm: goodsId
     * @return: Long
     */
    public Long getMiaoShaResult(Long userId, Long goodsId){
        MiaoShaOrder miaoShaResult = getMiaoShaOrderByUserIdGoodsId(userId, goodsId);
        if (notNull(miaoShaResult)){
            return miaoShaResult.getOrderId();
        } else {
            boolean isOver = miaoShaService.getGoodsOver(goodsId);
            if (isOver){
                return -1L;
            } else {
                return 0L;
            }
        }
    }
}
