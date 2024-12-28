package com.itheima.mp.service;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ClassName：IUserServiceTest
 *
 * @author: Devil
 * @Date: 2024/12/27
 * @Description:
 * @version: 1.0
 */
@SpringBootTest
class IUserServiceTest {
    @Autowired
    private IUserService iUserService;

    @Test
    void testSaveUser(){
        User user = new User();
        user.setUsername("Lilei");
        user.setPassword("123");
        user.setPhone("110");
        user.setBalance(200);
//        user.setInfo("{\"age\":24,\"intro\":\"英语老师\",\"gender\":\"female\"}");
        user.setInfo(UserInfo.of(24,"英语老师","female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        iUserService.save(user);
    }

    @Test
    void testQuery(){
        List<User> users = iUserService.listByIds(List.of(1L, 2L, 3L));
        users.forEach(System.out::println);
    }

    @Test
    void testPageQuery(){
        int pageNo = 1,pageSize = 2;
        //1.准备分页条件
        //1.1分页条件
        Page<User> page = Page.of(pageNo, pageSize);
        //1.2排序条件
        page.addOrder(new OrderItem("balance",true));
        page.addOrder(new OrderItem("id",true));

        //2.分页查询
        Page<User> userPage = iUserService.page(page);

        //3.解析
        long total = userPage.getTotal();
        System.out.println("total = "+total);
        long pages = userPage.getPages();
        System.out.println("pages = "+pages);
        List<User> users = userPage.getRecords();
        users.forEach(System.out::println);
    }
}