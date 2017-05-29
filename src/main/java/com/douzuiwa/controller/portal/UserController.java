package com.douzuiwa.controller.portal;

import com.douzuiwa.common.Const;
import com.douzuiwa.common.ServerResponse;
import com.douzuiwa.pojo.User;
import com.douzuiwa.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Gyges on 2017/5/29.
 * 用户模块接口
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> Login(String username, String password, HttpSession session){
        ServerResponse<User> response = null;
        try {
            response = iUserService.login(username,password);
            if (response.isSuccess()){
                session.setAttribute(Const.CURRENT_USER,response.getData());
            }
        }catch (Exception e){
            LOGGER.error("登录异常");
        }
        return response;
    }
}
