package com.itheima.publisher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.HashMap;
import java.util.Map;


/**
 * ClassName：SpringAmqpTest
 *
 * @author: Devil
 * @Date: 2025/1/6
 * @Description:
 * @version: 1.0
 */
@SpringBootTest
@Slf4j
class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSimpleQueue(){
        //1.队列名
        String queueName = "simple.queue";
        //2. 消息
        String message = "hello, spring amqp!";
        //3.发送消息
        rabbitTemplate.convertAndSend(queueName,message);
    }

    @Test
        public void testWorkQueue(){
        //1.队列名
        String queueName = "work.queue";
        for (int i = 1;i <= 50 ; i++){
            //2. 消息
            String message = "hello, spring amqp_" + i;
            //3.发送消息
            rabbitTemplate.convertAndSend(queueName,message);
        }
    }

    @Test
    public void testFanoutQueue(){
        //1.队列名
        String exchangeName = "hmall.fanout";
        //2. 消息
        String message = "hello, everyone!";
        //3.发送消息
        rabbitTemplate.convertAndSend(exchangeName,null,message);
    }

    @Test
    public void testDirectQueue(){
        //1.队列名
        String exchangeName = "hmall.direct";
        //2. 消息
        String message = "hello, 你好!";
        //3.发送消息
        rabbitTemplate.convertAndSend(exchangeName,"yellow",message);
    }

    @Test
    public void testTopicQueue(){
        //1.队列名
        String exchangeName = "hmall.topic";
        //2. 消息
        String message = "中国新闻!";
        //3.发送消息
        rabbitTemplate.convertAndSend(exchangeName,"china.news",message);
    }
    @Test
    public void testSendObject(){
        // 准备消息
        Map<String,Object> msg = new HashMap<>();
        msg.put("name", "柳岩");
        msg.put("age", 21);
        // 发送消息
        rabbitTemplate.convertAndSend("object.queue", msg);
    }

    @Test
    public void testSendDelayMessage(){
        rabbitTemplate.convertAndSend("normal.direct", "hi", "hello", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });
    }

    @Test
    public void testSendDelayMessageByPlugin(){
        rabbitTemplate.convertAndSend("delay.direct", "hi", "hello", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setDelay(10000);
                return message;
            }
        });
    }
}