package com.itheima.publisher.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * ClassName：MqConfig
 *
 * @author: Devil
 * @Date: 2025/1/7
 * @Description:
 * @version: 1.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class MqConfig {
    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setReturnsCallback(returned -> {
            log.error("触发return callback,");
            log.error("exchange: {}", returned.getExchange());
            log.error("routingKey: {}", returned.getRoutingKey());
            log.error("message: {}", returned.getMessage());
            log.error("replyCode: {}", returned.getReplyCode());
            log.error("replyText: {}", returned.getReplyText());
        });
    }
}
