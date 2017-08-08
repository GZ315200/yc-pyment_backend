package com.igeek.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
