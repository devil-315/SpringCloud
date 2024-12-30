package com.hmall.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

/**
 * ClassName：CartClient
 *
 * @author: Devil
 * @Date: 2024/12/30
 * @Description:
 * @version: 1.0
 */
@FeignClient("cart-service")
public interface CartClient {
    @DeleteMapping("/ids")
    void deleteCartItemByIds(@RequestParam("ids") Collection<Long> ids);
}
