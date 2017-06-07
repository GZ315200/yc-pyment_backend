package com.igeek.controller.portal;

import com.igeek.common.Const;
import com.igeek.common.ResponseCode;
import com.igeek.common.ServerResponse;
import com.igeek.pojo.User;
import com.igeek.service.portal.IUserService;
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
 * 用户模块接口(方法全设置为POST)
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     *
     * @param username 账户
     * @param password 密码
     * @param session
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = null;
        try {
            response = iUserService.login(username, password);
            if (response.isSuccess()) {
                session.setAttribute(Const.CURRENT_USER, response.getData());
            }
        } catch (Exception e) {
            LOGGER.error("登录异常", e);
        }
        return response;
    }

    /**
     * 注销
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "loginOut", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> loginOut(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }


    /**
     * 注册
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        ServerResponse<String> response = null;
        try {
            response = iUserService.register(user);
        } catch (Exception e) {
            LOGGER.error("注册异常", e);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 检验参数
     *
     * @param str  输入参数
     * @param type 输入类型
     * @return
     */
    @RequestMapping(value = "checkValid", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        ServerResponse<String> response = null;
        try {
            response = iUserService.checkValid(str, type);
        } catch (Exception e) {
            LOGGER.error("检验异常", e);
        }
        return response;
    }

    /**
     * 获得当前用户信息
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_info", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getCurrentUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMsg("用户未登录，无法获取当前用户");
    }

    /**
     * 获取用户的问题
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "get_question", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        ServerResponse<String> response = null;
        try {
            response = iUserService.searchQuestion(username);
        } catch (Exception e) {
            LOGGER.error("获取用户问题异常", e);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 检查问题答案是否一致
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "forget_get_answer", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 重置密码
     *
     * @param username
     * @param passwordNew
     * @param token
     * @return
     */
    @RequestMapping(value = "reset_password", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(String username, String passwordNew, String token) {
        return iUserService.resetPassword(username, passwordNew, token);
    }

    /**
     * 登录状态设置密码
     * @param passwordOld
     * @param passwordNew
     * @param session
     * @return
     */
    @RequestMapping(value = "login_reset_password", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> loginResetPassword(String passwordOld, String passwordNew, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMsg("当前用户不存在");
        }
        return iUserService.loginResetPassword(passwordOld, passwordNew, user);
    }

    /**
     * 更新用户信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "update_user_info", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInfo(HttpSession session,User user){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMsg("当前用户不存在");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateUserInfo(user);
//        如果用户信息更新成功，则更新用户
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    /**
     * 获取用户信息详情
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_detail", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserDetailInfo(HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,需要强制登录");
        }
        return iUserService.getUserInfo(currentUser.getId());
    }


}
