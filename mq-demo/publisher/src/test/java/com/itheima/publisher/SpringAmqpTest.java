package com.itheima.publisher;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ClassName：SpringAmqpTest
 *
 * @author: Devil
 * @Date: 2025/1/6
 * @Description:
 * @version: 1.0
 */
@SpringBootTest
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
}