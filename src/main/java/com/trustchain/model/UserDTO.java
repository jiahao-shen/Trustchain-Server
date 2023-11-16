package com.trustchain.model;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trustchain.mapper.UserMapper;

import java.util.List;

public class UserDTO extends User{

    public static List<User> getChildUser(UserMapper userMapper, Long organizationId){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getOrganization, organizationId);
        List<User> childUserList = userMapper.selectList(queryWrapper);
        return childUserList;
    }

}
