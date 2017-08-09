package com.igeek.service.system;

import com.igeek.common.ServerResponse;

/**
 * Created by Gyges on 2017/8/8.
 */
public interface IOrderService {

    public ServerResponse pay(Integer userId, Long orderNo, String path);
}
