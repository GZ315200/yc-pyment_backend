package com.douzuiwa.controller.backend;

import com.douzuiwa.common.Const;
import com.douzuiwa.common.ServerResponse;
import com.douzuiwa.common.TokenCache;
import com.douzuiwa.pojo.User;
import com.douzuiwa.service.system.impl.UserAdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by Gyges on 2017/5/30.
 */
@Controller
@RequestMapping("/manage/")
public class UserAdminController {

    @Resource
    private UserAdminService userAdminService;

    /**
     * 后台用户登录
     *
     * @param phone
     * @param messageCode
     * @param session
     * @return
     */
    @RequestMapping(value = "admin_user", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> adminLogin(String phone, String messageCode, HttpSession session) {

        if (StringUtils.isBlank(messageCode)) {
            return ServerResponse.createByErrorMsg("短信验证码不为空");
        }
        ServerResponse<User> response = userAdminService.adminLogin(phone);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response);
//            获取用户的messageCode
            String loginMessageCode = TokenCache.getValue("MESSAGE_" + phone);
            if (StringUtils.isBlank(loginMessageCode)) {
                return ServerResponse.createByErrorMsg("参数错误,需要传递参数");
            }
            if (loginMessageCode.equals(messageCode)) {
                return ServerResponse.createBySuccess("验证码正确");
            } else {
                return ServerResponse.createByErrorMsg("验证码无效，请重新获取");
            }
        } else {
            return ServerResponse.createByErrorMsg("该用户为登录，重新登录");
        }
    }

    /**
     * 获取短信验证码
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "get_message_code", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> getMessageCode(String phone) {
        return userAdminService.getMessageCode(phone);
    }
}
