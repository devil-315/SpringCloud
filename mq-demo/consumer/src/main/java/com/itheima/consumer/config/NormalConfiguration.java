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
public class NormalConfiguration {

    @Bean
    public DirectExchange normalExchange(){
        return new DirectExchange("normal.direct");
    }

    @Bean
    public Queue normalQueue1(){
        return QueueBuilder.durable("normal.queue").deadLetterExchange("dlx.direct").build();
    }

    @Bean
    public Binding noramlExchangeBinding(Queue normalQueue1, DirectExchange normalExchange){
        return BindingBuilder.bind(normalQueue1).to(normalExchange).with("hi");
    }
}
