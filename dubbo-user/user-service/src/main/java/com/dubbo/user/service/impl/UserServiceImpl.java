package com.dubbo.user.service.impl;

import com.dubbo.core.permission.Permission;
import com.dubbo.user.entity.SysUser;
import com.dubbo.user.mapper.UserMapper;
import com.dubbo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<SysUser> queryAll(Integer userId, String name) {
        return userMapper.queryAll(userId, name);
    }
}
