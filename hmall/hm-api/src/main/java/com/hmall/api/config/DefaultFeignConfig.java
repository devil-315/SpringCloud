package com.hmall.api.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * ClassName：DefaultFeignConfig
 *
 * @author: Devil
 * @Date: 2024/12/29
 * @Description:
 * @version: 1.0
 */
public class DefaultFeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
