package com.douzuiwa.controller.backend;

import com.douzuiwa.common.Const;
import com.douzuiwa.common.ResponseCode;
import com.douzuiwa.common.ServerResponse;
import com.douzuiwa.exception.GeneralServiceException;
import com.douzuiwa.pojo.User;
import com.douzuiwa.service.system.IUserAdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Created by Gyges on 2017/5/30.
 */
@Controller
@RequestMapping("/manage/")
public class UserAdminController {

    @Resource
    private IUserAdminService userAdminService;

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
        try {
            ServerResponse<User> response = userAdminService.adminLogin(phone,messageCode);
            if (response.isSuccess()) {
                session.setAttribute(Const.CURRENT_USER, response);
                return ServerResponse.createBySuccess("登录成功",response.getData());
            }
        }catch (Exception e){
            String msg = MessageFormat.format("登录异常,thread:{0}", Thread.currentThread().getName());
            throw new GeneralServiceException(msg,e);
        }
        return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), "用户未注册，请联系管理员");
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
        ServerResponse<String> response = null;
        try {
            response = userAdminService.getMessageCode(phone);
        } catch (IOException e) {
            String msg = MessageFormat.format("获取验证码异常 phone:{0}",phone);
            throw new GeneralServiceException(msg,e);
        }
        return response;
    }
}
