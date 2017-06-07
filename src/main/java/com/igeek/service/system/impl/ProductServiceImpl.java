package com.igeek.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.igeek.common.Const;
import com.igeek.common.ResponseCode;
import com.igeek.common.ServerResponse;
import com.igeek.dao.CategoryMapper;
import com.igeek.dao.ProductMapper;
import com.igeek.pojo.Category;
import com.igeek.pojo.Product;
import com.igeek.service.system.ICategoryService;
import com.igeek.service.system.IProductService;
import com.igeek.util.DateTimeUtil;
import com.igeek.util.PropertiesUtil;
import com.igeek.vo.ProductDetailVo;
import com.igeek.vo.ProductListVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gyges on 2017/6/6.
 */
@Service(value = "iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 更新或存储产品
     *
     * @param product
     * @return
     */
    public ServerResponse<String> updateOrSaveProduct(Product product) {
        int rowCount = 0;
        if (!Objects.equal(product, null)) {

            if(product.getSubImages() == null) {
                return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getCodeDesc());
            }
            String[] imageArray = product.getSubImages().split(",");
            if (imageArray.length > 0) {
                product.setMainImage(imageArray[0]);
            }
            if (product.getId() != null) {
                rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("更新产品信息成功");
                }
                return ServerResponse.createByErrorMsg("更新产品信息失败");
            } else {
                rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("新增产品信息成功");
                }
                return ServerResponse.createByErrorMsg("新增产品信息失败");
            }
        }
        return ServerResponse.createByErrorMsg("新增或更新产品不正确");
    }

    /**
     * 设置产品的状态
     *
     * @param productId
     * @param status
     * @return
     */
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getCodeDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新产品状态成功");
        }
        return ServerResponse.createByErrorMsg("更新产品状态失败");
    }

    /**
     * 获得产品详情
     *
     * @param productId
     * @return
     */
    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getCodeDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMsg("该商品已经下架");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    /***
     * 组装产品详情
     * @param product
     * @return
     */
    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setProductId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setName(product.getName());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setDetail(product.getDetail());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0);
        } else {
            productDetailVo.setParentCategoryId(product.getCategoryId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));

        return productDetailVo;
    }


    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        /**
         * 1 startPage--start
         * 2. 填充自己的sql查询逻辑
         * 3. pageHelper收尾
         */
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectProductList();
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductList(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }


    private ProductListVo assembleProductList(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setDetail(product.getDetail());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubImages(product.getSubImages());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        return productListVo;
    }


    public ServerResponse<PageInfo> searchProductList(String productName, Integer productId, int pageNum, int pageSize) {
        if (productName != null) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectListByProductNameAndProductId(productName, productId);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : productList) {
            ProductListVo productListVo = assembleProductList(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }


    public ServerResponse<ProductDetailVo> detail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getCodeDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMsg("该商品已经下架");
        }
//        判断商品状态
        if (!Objects.equal(product.getStatus(), Const.ProductDetailEnum.ON_SALE.getCode())) {
            return ServerResponse.createByErrorMsg(Const.ProductDetailEnum.OFF_SALE.getDesc());
        }

        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }


    public ServerResponse<PageInfo> getProductByKeywordCategory(Integer categoryId, String keyword, int pageNum,
                                                                int pageSize, String orderBy) {

        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode()
                    , ResponseCode.ILLEGAL_ARGUMENT.getCodeDesc());
        }
        List<Integer> categoryIdList = new ArrayList<>();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                //没有该分类，没有关键字，返回一个空值
                List<ProductListVo> productList = Lists.newArrayList();
                PageHelper.startPage(pageNum, pageSize);
                PageInfo pageInfo = new PageInfo(productList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.getCategoryAndDeepChildrenCategory(category != null ? category.getId() : null).getData();
        }

        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_ESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
//        搜索productId 和关键字 in 的 sql(判断该商品是否在该父级栏中)
        List<Product> productList = productMapper.selectListByNameAndCategoryIds(
                StringUtils.isBlank(keyword) ? null : keyword
                , categoryIdList.size() > 0 ? categoryIdList : null);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVo productListVo = assembleProductList(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }


}
