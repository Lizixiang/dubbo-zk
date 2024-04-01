package com.dubbo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dubbo.user.entity.Customer;
import com.dubbo.user.entity.SysUser;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerMapper extends BaseMapper<Customer> {

    @Select("select id from customer where 1 = 1")
    List<Long> list();

    // TODO: 2024/4/1 支持mybatis原生的CRUD接口
    // TODO: 2024/4/1 支持xml中的sql数据过滤

}
