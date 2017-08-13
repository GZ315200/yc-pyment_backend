package com.igeek.dao;

import com.igeek.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderItemMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    List<OrderItem> getOrderItemInfo(@Param("userId") Integer userId, @Param("orderId") Long orderId);

    List<OrderItem> checkParams(@Param("orderId") Long orderId, @Param("totalPrice")BigDecimal price);
}