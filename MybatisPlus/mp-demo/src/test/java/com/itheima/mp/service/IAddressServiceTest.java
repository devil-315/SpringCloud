package com.itheima.mp.service;

import com.itheima.mp.domain.po.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * ClassName：IAddressServiceTest
 *
 * @author: Devil
 * @Date: 2024/12/28
 * @Description:
 * @version: 1.0
 */
@SpringBootTest
public class IAddressServiceTest {

    @Autowired
    private IAddressService addressService;

    @Test
    void testLogicDelete(){
        addressService.removeById(1L);

        Address address = addressService.getById(1L);
        System.out.println("address = "+address);
    }
}
