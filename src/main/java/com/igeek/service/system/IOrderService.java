package com.igeek.service.system;

import com.igeek.common.ServerResponse;

import java.util.Map;

/**
 * Created by Gyges on 2017/8/8.
 */
public interface IOrderService {

    public ServerResponse pay(Integer userId, Long orderNo, String path);

    public ServerResponse aliPayCallback(Map<String,String> params);

    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);
}
