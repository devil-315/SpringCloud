package com.heima.item;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName：itemApplication
 *
 * @author: Devil
 * @Date: 2024/12/29
 * @Description:
 * @version: 1.0
 */
@MapperScan("com.heima.item.mapper")
@SpringBootApplication
public class ItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class,args);
    }
}
