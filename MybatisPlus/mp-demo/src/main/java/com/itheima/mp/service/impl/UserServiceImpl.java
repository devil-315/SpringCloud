package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassName：UserServiceImpl
 *
 * @author: Devil
 * @Date: 2024/12/27
 * @Description:
 * @version: 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    @Transactional
    public void deductBalance(Long id, Integer money) {
        //1.查询用户
        User user = getById(id);
        //2.校验用户状态
        if(user == null || user.getStatus() == UserStatus.FROZEN){
            throw new RuntimeException("用户状态异常！");
        }
        //3.校验月是否充足
        if(user.getBalance() < money){
            throw new RuntimeException("用户余额不足！");
        }
        //4.扣除余额
//        baseMapper.deductBalance(id,money);
        int remainBalance = user.getBalance() - money;
        lambdaUpdate()
                .set(User::getBalance,remainBalance)
                .set(remainBalance == 0, User::getStatus,UserStatus.FROZEN)
                .eq(User::getId,id)
                .eq(User::getBalance,user.getBalance())//乐观锁
                .update();
    }

    @Override
    public List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance) {
        return lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .ge(minBalance != null, User::getBalance, minBalance)
                .le(maxBalance != null, User::getBalance, maxBalance)
                .list();
    }

    @Override
    public UserVO queryUsersAndAdressById(Long id) {
        //1.查询用户
        User user = getById(id);
        if(user == null||user.getStatus() == UserStatus.FROZEN){
            throw new RuntimeException("用户状态异常！");
        }
        //2.查询地址
        List<Address> addresses = Db.lambdaQuery(Address.class).eq(Address::getUserId, id).list();
        //3.封装Vo
        //3.1转user的PO为VO
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        //3.2转地址VO
        if(CollUtil.isNotEmpty(addresses)){
            List<AddressVO> addressVOList = BeanUtil.copyToList(addresses, AddressVO.class);
            userVO.setAddress(addressVOList);
        }
        return userVO;
    }

    @Override
    public List<UserVO> queryUserAndAddressByIds(List<Long> ids) {
        //1.查询用户
        List<User> users = listByIds(ids);
        if(CollUtil.isEmpty(users)){
            return Collections.emptyList();
        }
        //2.查询地址
        //2.1获取用户id集合
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        //2.2根据用户id查询地址
        List<Address> addresses = Db.lambdaQuery(Address.class).in(Address::getUserId, userIds).list();
        //2.3转换地址VO
        List<AddressVO> addressVOList = BeanUtil.copyToList(addresses, AddressVO.class);
        //2.4用户地址集合分组处理，相同用户的放入一个集合（组）中
        Map<Long, List<AddressVO>> addressMap = new HashMap<>(0);
        if(CollUtil.isNotEmpty(addressVOList)){
            addressMap = addressVOList.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
        }

        //3.转换VO返回
        List<UserVO> list = new ArrayList<>(users.size());
        for (User user : users) {
            //3.转换user的PO为VO
            UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
            list.add(userVO);
            //3.2转换地址VO
            userVO.setAddress(addressMap.get(user.getId()));
        }
        return list;
    }

    @Override
    public PageDTO<UserVO> queryUserPage(UserQuery query) {
        String name = query.getName();
        Integer status = query.getStatus();
        //1.1构建分页条件
//        Page<User> page = Page.of(query.getPageNo(), query.getPageSize());
//        //1.2构建排序条件
//        if(StrUtil.isNotBlank(query.getSortBy())){
//            //不为空
//            page.addOrder(new OrderItem(query.getSortBy(),query.getIsAsc()));
//        }else {
//            //为空，默认安装更新时间排序
//            page.addOrder(new OrderItem("update_time",false));
//        }
        //1.构建条件
        Page<User> page = query.toMpPageDefaultSortByUpdateTimeDesc();

        //2.分页查询
        Page<User> userPage = lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .page(page);

        //3.封装VO结果
        return PageDTO.of(userPage, UserVO.class);
//        PageDTO<UserVO> dto = new PageDTO<>();
//        //3.1总条数
//        dto.setTotal(userPage.getTotal());
//        //3.2总页数
//        dto.setPages(userPage.getPages());
//        //3.3当前页数据
//        List<User> records = userPage.getRecords();
//        if(CollUtil.isEmpty(records)){
//            dto.setList(Collections.emptyList());
//            return dto;
//        }
//        //3.4拷贝user的Vo
//        List<UserVO> vos = BeanUtil.copyToList(records, UserVO.class);
//        dto.setList(vos);
//        //4.返回
//        return dto;
    }
}
