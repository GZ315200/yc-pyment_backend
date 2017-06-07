package com.igeek.service.system;

import com.github.pagehelper.PageInfo;
import com.igeek.common.ServerResponse;
import com.igeek.pojo.Product;
import com.igeek.vo.ProductDetailVo;

/**
 * Created by Gyges on 2017/6/6.
 */
public interface IProductService {

    public ServerResponse<String> updateOrSaveProduct(Product product);

    public ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    public ServerResponse<PageInfo> searchProductList(String productName, Integer productId, int pageNum, int pageSize);

    public ServerResponse<ProductDetailVo> detail(Integer productId);

    public ServerResponse<PageInfo> getProductByKeywordCategory(Integer categoryId,String keyword, int pageNum, int pageSize, String orderBy);

}
