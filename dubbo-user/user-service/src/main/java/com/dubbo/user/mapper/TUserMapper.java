package com.dubbo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dubbo.user.entity.TUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 *
 * @author lizixiang
 * @date 2024-04-10 17:45:43
 */
@Repository
public interface TUserMapper extends BaseMapper<TUser> {

    List<TUser> page();

}
