package com.dubbo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dubbo.user.entity.SysUser;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Permission
@Repository
public interface UserMapper extends BaseMapper<SysUser> {

    @Select("select su.id, su.name, su.age from sys_user su left join sys_user_group sug on su.id = sug.user_id")
//    @Permission
    List<SysUser> queryAll(Integer userId, String name);

}
