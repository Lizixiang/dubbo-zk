package com.dubbo.user.service.impl;

import com.dubbo.user.entity.Customer;
import com.dubbo.user.entity.SysUser;
import com.dubbo.user.mapper.CustomerMapper;
import com.dubbo.user.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<Long> list() {
        List<Long> list = customerMapper.list();
        return list;
    }
}
