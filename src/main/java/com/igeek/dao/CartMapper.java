package com.igeek.dao;

import com.igeek.pojo.Cart;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartList(@Param(value = "userId") Integer userId, @Param(value = "productId") Integer productId);

    List<Cart> selectCartListByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdProductIdList(@Param(value = "userId") Integer userId, @Param(value = "productIdList") List<String> productIdList);

    int updateCheckedOrUnChecked(@Param(value = "userId") Integer userId,@Param(value = "checked") Integer checked,@Param(value = "productId") Integer productId);

    int selectCountProduct(Integer userId);
}