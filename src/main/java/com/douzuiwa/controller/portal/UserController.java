package com.douzuiwa.controller.portal;

import com.douzuiwa.common.Const;
import com.douzuiwa.common.ServerResponse;
import com.douzuiwa.pojo.User;
import com.douzuiwa.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
     * @param username 账户
     * @param password 密码
     * @param session
     * @return
     */
    @RequestMapping(value = "login",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
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

    /**
     * 注销
     * @param session
     * @return
     */
    @RequestMapping(value = "loginOut",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> loginOut(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }


    /**
     * 注册
     * @param user
     * @return
     */
    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(@RequestBody User user){
        ServerResponse<String> response = null;
        try {
            response =  iUserService.register(user);
        } catch (Exception e) {
            LOGGER.error("注册异常");
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 检验参数
     * @param str 输入参数
     * @param type 输入类型
     * @return
     */
    @RequestMapping(value = "checkValid",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        ServerResponse<String> response = null;
        try {
            response = iUserService.checkValid(str,type);
        }catch (Exception e){
            LOGGER.error("检验异常");
        }
        return response;
    }
}
