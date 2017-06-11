package com.igeek.service.system;

import com.igeek.common.ServerResponse;
import com.igeek.vo.CartVo;

/**
 * Created by Gyges on 2017/6/8.
 */
public interface ICartService {

    public ServerResponse<CartVo> add(Integer count, Integer userId, Integer productId);

    public ServerResponse<CartVo> update(Integer count, Integer userId, Integer productId);

    public ServerResponse<CartVo> delete(Integer userId, String productIds);

    public ServerResponse<CartVo> getList(Integer userId);

    public ServerResponse<CartVo> updateCheckedOrUnChecked(Integer userId, Integer checked,Integer productId);

    public ServerResponse<Integer> getTotalProduct(Integer userId);

}
