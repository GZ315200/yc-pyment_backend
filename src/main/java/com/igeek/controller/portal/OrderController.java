package com.igeek.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.igeek.common.Const;
import com.igeek.common.ResponseCode;
import com.igeek.common.ServerResponse;
import com.igeek.pojo.User;
import com.igeek.service.system.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Gyges on 2017/8/3.
 */
@Controller
@RequestMapping("/order/")
public class OrderController {

    public static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;

    /**
     * 支付订单
     * @param session
     * @param orderNo
     * @param request
     * @return
     */
    @RequestMapping("pay")
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        String qRCodePath = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(user.getId(), orderNo, qRCodePath);
    }

    /**
     * 支付宝回调
     *
     * @param request
     * @return
     */
    @RequestMapping("alipay_callback")
    @ResponseBody
    public Object callback(HttpServletRequest request) {

        Map<String, String> params = Maps.newHashMap();

        Map param_map = request.getParameterMap();
        for (Object paramObject : param_map.keySet()) {
            String name = (String) paramObject;
            String[] values = request.getParameterValues(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
//                如果集合内有一个元素则直接等于该元素，如果不是则用，进行分隔连接
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{}, 参数:{}",params.get("sign"),params.get("trade_status"),params);

        //验证回调参数的正确性
        params.remove("sign_type");
        try {
            boolean alipaySignatureCheck = AlipaySignature.rsaCheckV2(params, Configs.getPublicKey(),"utf-8",Configs.getSignType());
            if (!alipaySignatureCheck){
                return ServerResponse.createByErrorMsg("非法请求参数不通过");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证参数失败",e);
        }
//       todo 校验通知数据的正确性

        ServerResponse serverResponse = iOrderService.aliPayCallback(params);

        if (serverResponse.isSuccess()){
            return Const.AlipayCallbackStatus.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallbackStatus.RESPONSE_FAILED;
    }


    /***
     * 查询订单的支付状态
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("query_order_pay_status")
    @ResponseBody
        public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getCodeDesc());
        }
        ServerResponse response = iOrderService.queryOrderPayStatus(user.getId(),orderNo);
        if (response.isSuccess()) {
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }


}
