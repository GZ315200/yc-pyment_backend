package com.igeek.dao;

import com.igeek.pojo.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectProductList();

    List<Product> selectListByProductNameAndProductId(@Param("productName") String productName,
                                                      @Param("productId") Integer productId);

    List<Product> selectListByNameAndCategoryIds(@Param("productName") String productName,
                                                 @Param("categoryIdList") List<Integer> categoryList);
}