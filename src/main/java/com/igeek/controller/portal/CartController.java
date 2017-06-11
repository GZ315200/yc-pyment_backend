package com.igeek.controller.portal;

import com.igeek.common.Const;
import com.igeek.common.ResponseCode;
import com.igeek.common.ServerResponse;
import com.igeek.pojo.User;
import com.igeek.service.system.ICartService;
import com.igeek.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Gyges on 2017/6/8.
 */

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;


    /**
     * 获取产品列表
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "list")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        return iCartService.getList(user.getId());
    }

    /**
     * 添加购物车商品
     *
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping(value = "add")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        return iCartService.add(count, user.getId(), productId);
    }

    /**
     * 更新购物车信息
     *
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping(value = "update")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        return iCartService.update(count, user.getId(), productId);
    }

    /**
     * 删除购物车产品
     *
     * @param session
     * @param productIds
     * @return
     */
    @RequestMapping(value = "delete_product")
    @ResponseBody
    public ServerResponse<CartVo> delete(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        return iCartService.delete(user.getId(), productIds);
    }




    /**
     * todo 全选
     *  todo 全反选
     *  todo 单选
     *  todo 单独反选
     *  todo 查询当前用户购物车里的数量，如果一个产品有10个，那么数量就是10
      */

    /**
     * 全选
     * @param session
     * @return
     */
    @RequestMapping(value = "select_all")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        return iCartService.updateCheckedOrUnChecked(user.getId(),Const.Cart.CHECKED,null);
    }

    /**
     * 全反选
     * @param session
     * @return
     */
    @RequestMapping(value = "un_select_all")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        return iCartService.updateCheckedOrUnChecked(user.getId(),Const.Cart.UNCHECKED,null);
    }

    /**
     * 单选一个商品
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "single_select")
    @ResponseBody
    public ServerResponse<CartVo> singleSelect(HttpSession session,Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        return iCartService.updateCheckedOrUnChecked(user.getId(),Const.Cart.CHECKED,productId);
    }

    /**
     * 反选一个商品
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "un_single_select")
    @ResponseBody
    public ServerResponse<CartVo> unSingleSelect(HttpSession session,Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        return iCartService.updateCheckedOrUnChecked(user.getId(),Const.Cart.UNCHECKED,productId);
    }


    /**
     * 获取购物车产品总数
     * @param session
     * @return
     */
    @RequestMapping(value = "get_total")
    @ResponseBody
    public ServerResponse<Integer> getCartProductTotal(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getTotalProduct(user.getId());
    }



}
