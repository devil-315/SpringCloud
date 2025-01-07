package com.itheima.consumer.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName：FanoutConfiguration
 *
 * @author: Devil
 * @Date: 2025/1/6
 * @Description:
 * @version: 1.0
 */
@Configuration
public class DirectConfiguration {

    @Bean
    public DirectExchange directExchange(){
//        return ExchangeBuilder.fanoutExchange("hmall.fanout").build();
        return new DirectExchange("hmall.direct");
    }

    @Bean
    public Queue directQueue1(){
//        return QueueBuilder.durable("fanout.queue1").build();
        return new Queue("direct.queue1");
    }

//    @Bean
//    public Binding directQueue1BindingRed(Queue fanoutQueue1,DirectExchange directExchange){
//        return BindingBuilder.bind(fanoutQueue1).to(directExchange).with("red");
//    }
//
//    @Bean
//    public Binding directQueue1BindingBlue(Queue fanoutQueue1,DirectExchange directExchange){
//        return BindingBuilder.bind(fanoutQueue1).to(directExchange).with("blue");
//    }

    @Bean
    public Queue directQueue2(){
//        return QueueBuilder.durable("fanout.queue2").build();
        return new Queue("direct.queue2");
    }

//    @Bean
//    public Binding directQueue2BindingRed(Queue directQueue2,DirectExchange directExchange){
//        return BindingBuilder.bind(directQueue2).to(directExchange).with("red");
//    }
//
//    @Bean
//    public Binding directQueue2BindingYellow(Queue directQueue2,DirectExchange directExchange){
//        return BindingBuilder.bind(directQueue2).to(directExchange).with("yellow");
//    }
}
