package com.dubbo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dubbo.user.entity.UserState;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Description:
 *
 * @author lizixiang
 * @date 2024-04-10 17:45:43
 */
@Repository
public interface UserStateMapper extends BaseMapper<UserState> {

    void batchInsert(@Param("list") Collection<UserState> col);

}
