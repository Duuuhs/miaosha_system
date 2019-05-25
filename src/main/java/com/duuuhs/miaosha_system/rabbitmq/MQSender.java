package com.duuuhs.miaosha_system.rabbitmq;

import com.duuuhs.miaosha_system.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: DMY
 * @Date: 2019/4/22 23:57
 * @Description: mq发送者
 */
@Service
public class MQSender {

    private Logger logger = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private AmqpTemplate amqpTemplate;


    public void sendMiaoShaMessage(MiaoShaMessage mm) {
        String msg = RedisService.beanToStr(mm);
        logger.info(" send message: "+ msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
    }




    //注释掉四种交换机sender的代码
    /*
    public void send(Object object){
        String message = RedisService.beanToStr(object);
        logger.info("send message:" + message);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, message);
    }

    public void sendTopic(Object object){
        String message = RedisService.beanToStr(object);
        logger.info("send topic message:"+message);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", message+"_1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", message+"_2");
    }

    public void sendFanout(Object object){
        String message = RedisService.beanToStr(object);
        logger.info("send fanout message:"+message);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", message);
    }

    public void sendHeader(Object object){
        String message = RedisService.beanToStr(object);
        logger.info("send header message:"+message);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("header1", "value1");
        messageProperties.setHeader("header2", "value2");
        Message obj = new Message(message.getBytes(),messageProperties );
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
    }
    */


}
