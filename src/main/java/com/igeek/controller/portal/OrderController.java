package com.igeek.controller.portal;

import com.igeek.common.Const;
import com.igeek.common.ResponseCode;
import com.igeek.common.ServerResponse;
import com.igeek.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Gyges on 2017/8/3.
 */
@Controller
@RequestMapping("/order/")
public class OrderController {

    @RequestMapping("callback")
    @ResponseBody
    public String alipayCallback(){
        return null;
    }


    /**
     * 支付订单
     * @param session
     * @param orderNo
     * @param request
     * @return
     */
    @RequestMapping("pay")
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        String qRCodePath = request.getServletContext().getRealPath("upload");
        return null;
    }
}
