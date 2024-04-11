package com.dubbo.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dubbo.user.entity.TUser;

import java.util.List;

/**
 * Description:
 *
 * @author lizixiang
 * @date 2024-04-10 17:45:01
 */
public interface TUserService extends IService<TUser> {

    List<TUser> page();

}
