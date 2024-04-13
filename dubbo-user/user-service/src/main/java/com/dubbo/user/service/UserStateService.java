package com.dubbo.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dubbo.user.entity.UserState;

import java.util.Collection;

/**
 * Description:
 *
 * @author lizixiang
 * @date 2024-04-12 16:28:12
 */
public interface UserStateService extends IService<UserState> {

    void batchInsert(Collection<UserState> col);

}
