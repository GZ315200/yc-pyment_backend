package com.igeek.controller.portal;

import com.github.pagehelper.PageInfo;
import com.igeek.common.Const;
import com.igeek.common.ResponseCode;
import com.igeek.common.ServerResponse;
import com.igeek.pojo.Shipping;
import com.igeek.pojo.User;
import com.igeek.service.portal.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Gyges on 2017/6/10.
 */
@Controller
@RequestMapping(value = "/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    /**
     * 新增收货地址
     * @param session
     * @param shipping
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ServerResponse add(HttpSession session,Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        return iShippingService.addShipping(user.getId(),shipping);
    }






    /**
     * 删除地址
     * @param session
     * @param id
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ServerResponse del(HttpSession session,Integer id) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        return iShippingService.delShipping(user.getId(),id);
    }


    /**
     * 更新收货地址
     * @param session
     * @param shipping
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ServerResponse update(HttpSession session,Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        return iShippingService.updateShipping(user.getId(),shipping);
    }



    /**
     * 列出收货地址
     * @param session
     * @param
     * @return
     */
    @RequestMapping("list_shipping")
    @ResponseBody
    public ServerResponse<Shipping> list(HttpSession session,Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        return iShippingService.listShipping(user.getId(),shippingId);
    }


    /**
     * 地址分页
     * @param pageNum
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping("pageList")
    @ResponseBody
    public ServerResponse<PageInfo> pageList(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                             @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                             HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        return iShippingService.listPage(pageNum,pageSize,user.getId());
    }


}
