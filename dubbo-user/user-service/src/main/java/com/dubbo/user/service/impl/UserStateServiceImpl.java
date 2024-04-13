package com.dubbo.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dubbo.user.entity.UserState;
import com.dubbo.user.mapper.UserStateMapper;
import com.dubbo.user.service.UserStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Description:
 *
 * @author lizixiang
 * @date 2024-04-12 16:29:35
 */
@Service
public class UserStateServiceImpl extends ServiceImpl<UserStateMapper, UserState> implements UserStateService {

    @Autowired
    private UserStateMapper userStateMapper;

    @Override
    public void batchInsert(Collection<UserState> col) {
        userStateMapper.batchInsert(col);
    }
}
