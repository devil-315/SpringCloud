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
public class FanoutConfiguration {

    @Bean
    public FanoutExchange fanoutExchange(){
//        return ExchangeBuilder.fanoutExchange("hmall.fanout").build();
        return new FanoutExchange("hmall.fanout");
    }

    @Bean
    public Queue fanoutQueue1(){
//        return QueueBuilder.durable("fanout.queue1").build();
        return new Queue("fanout.queue1");
    }

    @Bean
    public Binding fanoutQueue1Binding(Queue fanoutQueue1,FanoutExchange fanoutExchange){
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    @Bean
    public Queue fanoutQueue2(){
//        return QueueBuilder.durable("fanout.queue2").build();
        return new Queue("fanout.queue2");
    }

    @Bean
    public Binding fanoutQueue2Binding(Queue fanoutQueue2,FanoutExchange fanoutExchange){
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }
}
