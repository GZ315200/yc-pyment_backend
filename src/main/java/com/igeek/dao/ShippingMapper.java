package com.igeek.dao;

import com.igeek.pojo.Shipping;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int updateByShipping(Shipping record);

    int deleteByShippingIdAndUserId(@Param(value = "userId") Integer userId,@Param(value = "shippingId") Integer shippingId);

    Shipping selectShippingByUserIdAndShippingId(@Param(value = "shippingId") Integer shippingId, @Param(value = "userId") Integer userId);

    List<Shipping> selectShippingByUserId(Integer userId);

}