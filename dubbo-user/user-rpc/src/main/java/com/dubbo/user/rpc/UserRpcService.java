package com.dubbo.user.rpc;

import com.dubbo.core.exception.ServiceException;

import java.util.List;

/**
 * @author lizixiang
 * @since 2021/2/6
 */
public interface UserRpcService {

    String getUser(Integer uid) throws ServiceException;

    void dubboRpc();

    List queryAll();

}
