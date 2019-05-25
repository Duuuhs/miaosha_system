package com.duuuhs.miaosha_system.controller;

import com.duuuhs.miaosha_system.rabbitmq.MQSender;
import com.duuuhs.miaosha_system.result.CodeMsg;
import com.duuuhs.miaosha_system.result.Reslut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: DMY
 * @Date: 2019/4/23 0:20
 * @Description:
 */
@Controller
@RequestMapping("mq")
public class MQController {

    @Autowired
    private MQSender mqSender;

    //测试四种交换机
    /*
    @RequestMapping("send")
    @ResponseBody
    public Reslut<String> send(){
        String message = "send test success";
        mqSender.send(message);
        return Reslut.success(CodeMsg.SUCCESS, message);
    }

    @RequestMapping("send/topic")
    @ResponseBody
    public Reslut<String> sendTopic(){
        String message = "send topic test ";
        mqSender.sendTopic(message);
        return Reslut.success(CodeMsg.SUCCESS, message);
    }

    @RequestMapping("send/fanout")
    @ResponseBody
    public Reslut<String> sendFanout(){
        String message = "send fanout test ";
        mqSender.sendFanout(message);
        return Reslut.success(CodeMsg.SUCCESS, message);
    }

    @RequestMapping("send/header")
    @ResponseBody
    public Reslut<String> sendHeader(){
        String message = "send header test ";
        mqSender.sendHeader(message);
        return Reslut.success(CodeMsg.SUCCESS, message);
    }
    */
}
