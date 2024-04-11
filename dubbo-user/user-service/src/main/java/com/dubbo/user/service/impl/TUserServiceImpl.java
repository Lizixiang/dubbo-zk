package com.dubbo.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dubbo.user.entity.TUser;
import com.dubbo.user.mapper.TUserMapper;
import com.dubbo.user.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author lizixiang
 * @date 2024-04-10 17:46:28
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {

    @Autowired
    private TUserMapper tUserMapper;

    @Override
    public List<TUser> page() {
        return tUserMapper.page();
    }
}
