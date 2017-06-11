package com.igeek.service.portal;

import com.github.pagehelper.PageInfo;
import com.igeek.common.ServerResponse;
import com.igeek.pojo.Shipping;

/**
 * Created by Gyges on 2017/6/10.
 */
public interface IShippingService {

    public ServerResponse addShipping(Integer userId,Shipping shipping);

    public ServerResponse<String> delShipping(Integer userId,Integer id);

    public ServerResponse<String> updateShipping(Integer userId,Shipping shipping);

    public ServerResponse<Shipping> listShipping(Integer userId,Integer shipping);

    public ServerResponse<PageInfo> listPage(int pageNum,int pageSize,Integer userId);
}
