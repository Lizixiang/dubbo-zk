package com.dubbo.user.service;

import com.dubbo.user.entity.SysUser;

import java.util.List;

public interface UserService {

    List<SysUser> queryAll(Integer userId, String name);

}
