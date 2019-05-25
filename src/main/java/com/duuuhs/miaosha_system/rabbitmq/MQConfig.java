package com.duuuhs.miaosha_system.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: DMY
 * @Date: 2019/4/23 0:04
 * @Description:
 */
@Configuration
public class MQConfig {

    public static final String MIAOSHA_QUEUE = "miaosha.queue";

    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String HEADER_QUEUE = "header.queue2";
    public static final String TOPIC_EXCHANGE = "topic.exchange";
    public static final String FANOUT_EXCHANGE = "fanout.exchange";
    public static final String HEADERS_EXCHANGE = "headers.exchange";




    @Bean
    public Queue miaoShaQueue(){
        return new Queue(MIAOSHA_QUEUE, true);
    }



    /*
     * Exchange交换机模式--第一种：Direct模式
     */
    @Bean
    public Queue queue(){
        return new Queue(QUEUE, true);
    }


    /*
     * Exchange交换机模式--第二种：Topic模式
     * 消息需要先放在exchange中，exchange再将消息传递给queue
     */
    @Bean
    public Queue topicQueue1(){
        return new Queue(TOPIC_QUEUE1, true);
    }

    @Bean
    public Queue topicQueue2(){
        return new Queue(TOPIC_QUEUE2, true);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
    }

    @Bean
    public Binding topicBinding2(){
        //通配符匹配多个key
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
    }


    /*
     * Exchange交换机模式--第三种：Fanout模式
     * 消息需要先放在exchange中，exchange再将消息传递给queue
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinging1(){
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBinging2(){
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }

    /*
     * Exchange交换机模式--第四种：Headers模式
     * 消息需要先放在exchange中，exchange再将消息传递给queue
     */
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);

    }

    @Bean
    public Queue headerQueue(){
        return new Queue(HEADER_QUEUE, true);
    }

    @Bean
    public Binding headersBinging1(){
        Map<String, Object> map = new HashMap<>();
        map.put("header1", "value1");
        map.put("header2", "value2");
        return BindingBuilder.bind(headerQueue()).to(headersExchange()).whereAll(map).match();
    }

}
