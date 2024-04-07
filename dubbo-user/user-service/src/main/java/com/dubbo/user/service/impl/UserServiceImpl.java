package com.dubbo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dubbo.user.entity.SysUser;
import com.dubbo.user.mapper.UserMapper;
import com.dubbo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<SysUser> queryAll(Integer userId, String name) {
        return userMapper.queryAll(userId, name);
    }

    // 查询所有用户的信息并缓存，这个地方只是举个例子，其实这个操作是不可取的，因为实际生产中用户数据是不可控的，不建议将全部用户数据存放到内存中
    @Cacheable(cacheNames = "user", key = "'user_info'", cacheManager = "l2CacheManager")
    @Override
    public List<SysUser> queryAll() {
        return userMapper.selectList(new QueryWrapper<>());
    }

    @Cacheable(cacheNames = "user", key = "T(com.dubbo.user.enums.CacheEnum).USER.getFormattedKey(#userId)", cacheManager = "l2CacheManager")
    @Override
    public SysUser queryById(Integer userId) {
        return userMapper.selectById(userId);
    }

    @CacheEvict(cacheNames = "user", key = "T(com.dubbo.user.enums.CacheEnum).USER.getFormattedKey(#userId)", cacheManager = "l2CacheManager")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysUser updateNameById(Integer userId, String name) {
        SysUser sysUser = userMapper.selectById(userId);
        if (sysUser != null) {
            sysUser.setName(name);
            userMapper.updateById(sysUser);
//            int i = 1 / 0;
        }
        return sysUser;
    }
}
