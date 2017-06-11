package com.igeek.service.system.impl;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.igeek.common.Const;
import com.igeek.common.ResponseCode;
import com.igeek.common.ServerResponse;
import com.igeek.dao.CartMapper;
import com.igeek.dao.ProductMapper;
import com.igeek.pojo.Cart;
import com.igeek.pojo.Product;
import com.igeek.service.system.ICartService;
import com.igeek.util.BigDecimalUtil;
import com.igeek.util.PropertiesUtil;
import com.igeek.vo.CartProductVo;
import com.igeek.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Gyges on 2017/6/8.
 */
@Service(value = "iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 添加购物车商品
     * @param count
     * @param userId
     * @param productId
     * @return
     */
    public ServerResponse<CartVo> add(Integer count, Integer userId, Integer productId) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getCodeDesc());
        }
        Cart cart = cartMapper.selectCartList(userId, productId);
        if (Objects.equal(cart, null)) {
//         如果该产品不在购物车里，需要新增一个这个产品的记录
            Cart cartItem = new Cart();
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartMapper.insert(cartItem);
        } else {
//            产品已经在购物车里
//            如果产品存在数量相加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
//            更新产品数量
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.getList(userId);
    }



    public ServerResponse<CartVo> update(Integer count, Integer userId, Integer productId){
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getCodeDesc());
        }
        Cart cart = cartMapper.selectCartList(userId, productId);
        if (!Objects.equal(cart,null)){
            cart.setQuantity(count);//更新购物车产品数量
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.getList(userId);
    }


    public ServerResponse<CartVo> delete(Integer userId,String productIds){
//       传过来的数用逗号分隔
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productList) && userId == null){
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getCodeDesc());
        }
        cartMapper.deleteByUserIdProductIdList(userId,productList);
        return this.getList(userId);
    }


    /**
     * 正选，反选一个商品
     * @param userId
     * @param checked
     * @param productId
     * @return
     */
    public ServerResponse<CartVo> updateCheckedOrUnChecked(Integer userId,Integer checked,Integer productId){
        cartMapper.updateCheckedOrUnChecked(userId, checked,productId);
        return this.getList(userId);
    }

    /**
     * 获取产品数量
     * @param userId
     * @return
     */
    public ServerResponse<Integer> getTotalProduct(Integer userId){
        if (userId == null){
            return ServerResponse.createBySuccess(0);
        }
        cartMapper.selectCountProduct(userId);
        return ServerResponse.createBySuccess(cartMapper.selectCountProduct(userId));
    }








    /**
     * 获取购物车列表
     * @param userId
     * @return
     */
    public ServerResponse<CartVo> getList(Integer userId){
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }


    /**
     * 获得购物车增加过限制的vo对象
     *
     * @param userId
     * @return
     */
    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartListByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

//        初始化购物车总价
        BigDecimal cartTotalPrice = new BigDecimal("0");

        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setProductId(cartItem.getProductId());
//        查询产品信息
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (!Objects.equal(product, null)) {
//                    组装产品信息
                    cartProductVo.setGetProductMainImage(product.getMainImage());//产品主图
                    cartProductVo.setProductName(product.getName());//产品名称
                    cartProductVo.setProductStatus(product.getStatus());//产品状态
                    cartProductVo.setProductSubtitle(product.getSubtitle());//产品子标题
                    cartProductVo.setProductPrice(product.getPrice());//产品单价

//                    限制购买数
                    int buyLimitCount = 0;
//                     库存充足
                    if (product.getStock() >= cartItem.getQuantity()) {
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_SUCCESS);
                    } else {
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_FAILED);
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
//                        更新产品库存
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);
//                     计算总价 = 产品单价  x  产品数量
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                //如果已选中购物车产品
                if (cartItem.getChecked() == Const.Cart.CHECKED) {
//                    购物车所有产品总价格 = 初始化购物车产品总价格 + 产品总价格
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
            cartVo.setCartProductVoList(cartProductVoList);
            cartVo.setCartTotalPrice(cartTotalPrice);
            cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
            cartVo.setAllChecked(this.getAllCheckedProductStatusByUserId(userId));

        return cartVo;
    }

    /**
     * 获取全选状态
     * @param userId
     * @return
     */
    private boolean getAllCheckedProductStatusByUserId(Integer userId){
        if (userId == null){
            return false;
        }
//        查询为0的话则已经被全选
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
    }
}
