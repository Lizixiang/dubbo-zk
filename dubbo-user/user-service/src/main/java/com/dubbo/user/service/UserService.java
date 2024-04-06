package com.dubbo.user.service;

import com.dubbo.user.entity.SysUser;

import java.util.List;

public interface UserService {

    List<SysUser> queryAll(Integer userId, String name);

    List<SysUser> queryAll();

    SysUser queryById(Integer userId);

    SysUser updateNameById(Integer userId, String name);

}
