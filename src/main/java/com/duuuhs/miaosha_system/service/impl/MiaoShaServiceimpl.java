package com.duuuhs.miaosha_system.service.impl;

import com.duuuhs.miaosha_system.model.OrderInfo;
import com.duuuhs.miaosha_system.model.User;
import com.duuuhs.miaosha_system.redis.GoodsKey;
import com.duuuhs.miaosha_system.redis.OrderKey;
import com.duuuhs.miaosha_system.redis.RedisService;
import com.duuuhs.miaosha_system.service.GoodsService;
import com.duuuhs.miaosha_system.service.MiaoShaService;
import com.duuuhs.miaosha_system.service.OrderService;
import com.duuuhs.miaosha_system.util.MD5Util;
import com.duuuhs.miaosha_system.util.UUIDUtil;
import com.duuuhs.miaosha_system.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.duuuhs.miaosha_system.util.Assert.isNull;
/**
 * @Author: DMY
 * @Date: 2019/4/19 0:01
 * @Description:
 */
@Service
public class MiaoShaServiceimpl implements MiaoShaService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    /*
     * 秒杀商品
     * @parm: user
     * @parm: goods
     * return: OrderInfo
     */
    @Override
    @Transactional
    public OrderInfo miaosha(User user, GoodsVo goods){
        //减库存 下订单 写入秒杀订单 (事务原子性)
        boolean success = goodsService.reduceStock(goods);
        //order_info miaosha_order
        if (success) {
            OrderInfo orderInfo = orderService.createOrder(user, goods);
            //订单存进redis
            redisService.set(OrderKey.getOrderByUserIdGoodsId,
                    "_" + orderInfo.getUserId() + "_" + orderInfo.getGoodsId(), orderInfo);
            return orderInfo;
        } else {
            //售完redis标记
            redisService.set(GoodsKey.isGoodsOver, ""+goods.getId(), true);
            return null;
        }
    }


    /*
     * redis标记某件秒杀商品售完
     * @parm: goodsId
     */
    public void setGoodsOver(Long goodsId){
        redisService.set(GoodsKey.isGoodsOver, ""+goodsId, true);
    }



    /*
     * redis查找某件秒杀商品是否售完
     * @parm: goodsId
     */
    public boolean getGoodsOver(Long goodsId){
        return redisService.exist(GoodsKey.isGoodsOver, ""+goodsId);
    }



    /*
     * 创建秒杀地址
     * @parm: user
     * @parm: goodsId
     * @return: String
     */
    public String createPath(User user, Long goodsId){
        String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisService.set(GoodsKey.getMiaoShaPath, ""+user.getUserId()+"_"+goodsId, str);
        return str;
    }


    /*
     * 验证秒杀path
     * @parm: user
     * @parm: goodsId
     * @parm: path
     * @return: boolean
     */
    public boolean checkPath(User user, Long goodsId, String path){
        if (isNull(user) || isNull(goodsId) || isNull(path)){
            return false;
        }
        String pathOld = redisService.get(GoodsKey.getMiaoShaPath, ""+user.getUserId()+"_"+goodsId, String.class);
        return path.equals(pathOld);
    }
}
