package com.duuuhs.miaosha_system.rabbitmq;

import com.duuuhs.miaosha_system.model.MiaoShaOrder;
import com.duuuhs.miaosha_system.model.OrderInfo;
import com.duuuhs.miaosha_system.model.User;
import com.duuuhs.miaosha_system.redis.OrderKey;
import com.duuuhs.miaosha_system.redis.RedisService;
import com.duuuhs.miaosha_system.result.CodeMsg;
import com.duuuhs.miaosha_system.result.Reslut;
import com.duuuhs.miaosha_system.service.GoodsService;
import com.duuuhs.miaosha_system.service.MiaoShaService;
import com.duuuhs.miaosha_system.service.OrderService;
import com.duuuhs.miaosha_system.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static com.duuuhs.miaosha_system.util.Assert.isNull;
import static com.duuuhs.miaosha_system.util.Assert.notNull;

/**
 * @Author: DMY
 * @Date: 2019/4/22 23:58
 * @Description: mq接收者
 */
@Service
public class MQReceiver {

    private Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MiaoShaService miaoShaService;



    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void miaoshaReceiver(String msg){
        logger.info("receiver message:" + msg);
        MiaoShaMessage mm = RedisService.strToBean(msg, MiaoShaMessage.class);
        User user = mm.getUser();
        Long goodsId = mm.getGoodsId();
        //判断秒杀的库存
        GoodsVo goods = goodsService.getMiaoShaById(goodsId);
        int stockCount = goods.getStockCount();
        if (stockCount <= 0){
            return;
        }
        //判断是否用户是否秒杀过了,redis中取
        OrderInfo getOrderInfoByRedis = redisService.get(OrderKey.getOrderByUserIdGoodsId,
                "_" + user.getUserId() + "_" + goodsId, OrderInfo.class);
        if (notNull(getOrderInfoByRedis)){
            return;
        }
        //减库存 下订单 写入秒杀订单 (事务原子性)
        OrderInfo orderInfo = miaoShaService.miaosha(user, goods);
        logger.info(user.getUserId() + "用户秒杀到了,订单号为：" + orderInfo.getId());


    }


    //注释掉四种交换机receiver的代码
    /*
    @RabbitListener(queues = MQConfig.QUEUE)
    public void receiver(String message){
        logger.info("receiver message:" + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiverTopic1(String message){
        logger.info("receiver topic queue1 message:" + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiverTopic2(String message){
        logger.info("receiver topic queue2 message:" + message);
    }

    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void receiverHeader(byte[] message){
        logger.info("receiver header queue message:" + new String(message));
    }
    */
}
