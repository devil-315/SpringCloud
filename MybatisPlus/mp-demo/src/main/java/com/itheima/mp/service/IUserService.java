package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;

import java.util.List;

/**
 * ClassName：IUserService
 *
 * @author: Devil
 * @Date: 2024/12/27
 * @Description:
 * @version: 1.0
 */
public interface IUserService extends IService<User> {

    void deductBalance(Long id, Integer money);

    List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance);

    UserVO queryUsersAndAdressById(Long id);

    List<UserVO> queryUserAndAddressByIds(List<Long> ids);

    PageDTO<UserVO> queryUserPage(UserQuery query);
}
