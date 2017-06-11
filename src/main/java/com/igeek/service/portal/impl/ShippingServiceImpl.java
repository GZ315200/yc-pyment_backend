package com.igeek.service.portal.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.igeek.common.ServerResponse;
import com.igeek.dao.ShippingMapper;
import com.igeek.pojo.Shipping;
import com.igeek.service.portal.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Gyges on 2017/6/10.
 */
@Service(value = "iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;





    public ServerResponse addShipping(Integer userId,Shipping shipping) {
        shipping.setUserId(userId);
//        插入行，通过useGeneratedKeys来给前端返回id
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("shippingId",shipping.getId());
            return ServerResponse.createBySuccess("新建收货地址成功",resultMap);
        }
        return ServerResponse.createByErrorMsg("增加收货地址失败");
    }




    @Override
    public ServerResponse<String> delShipping(Integer userId,Integer id) {
//        存在横向越权，通过userId、id定位并删除收货地址
        int rowCount = shippingMapper.deleteByShippingIdAndUserId(userId, id);
        if (rowCount > 0){
            return ServerResponse.createBySuccess("删除收货地址成功");
        }
        return ServerResponse.createByErrorMsg("删除收获地址失败");
    }






    @Override
    public ServerResponse<String> updateShipping(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
//        避免横向越权
        int rowCount = shippingMapper.updateByShipping(shipping);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新收货地址成功");
        }
        return ServerResponse.createByErrorMsg("更新收货地址失败");
    }





    @Override
    public ServerResponse<Shipping> listShipping(Integer userId, Integer shippingId) {
        Shipping shippingList = shippingMapper.selectShippingByUserIdAndShippingId(shippingId,userId);
        if (shippingList == null){
            return ServerResponse.createByErrorMsg("无法获取收货地址");
        }
        return ServerResponse.createBySuccess("获取收货地址成功",shippingList);
    }






    @Override
    public ServerResponse<PageInfo> listPage(int pageNum, int pageSize, Integer userId) {
//        开始分页的页数和容量
        PageHelper.startPage(pageNum, pageSize);
//        查询要分页的对象
        List<Shipping> shippingList = shippingMapper.selectShippingByUserId(userId);
//        创建pageInfo
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }




}
