package com.douzuiwa.service.portal.impl;

import com.douzuiwa.common.Const;
import com.douzuiwa.common.ServerResponse;
import com.douzuiwa.common.TokenCache;
import com.douzuiwa.dao.UserMapper;
import com.douzuiwa.pojo.User;
import com.douzuiwa.service.portal.IUserService;
import com.douzuiwa.util.MD5Util;
import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Gyges on 2017/5/29.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    private static Logger Logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) throws Exception {
        int userCount = userMapper.selectByUsername(username);
        Logger.info("登录的账号数: " + userCount);
        if (userCount == 0) {
            return ServerResponse.createByErrorMsg("账号密码错误");
        }
//        todo 密码验证
        String md5Password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMsg("登录密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) throws Exception {

        ServerResponse<String> response = null;
        int returnCount = 0;
// TODO: 2017/5/30 检验uername
        response = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!response.isSuccess()) {
            return response;
        }

//        设置密码
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        // TODO: 2017/5/30  检验email
        response = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!response.isSuccess()) {
            return response;
        }
// TODO: 2017/5/30 检验phone
        response = this.checkValid(user.getPhone(), Const.PHONE);
        if (!response.isSuccess()) {
            return response;
        }

//        设置角色 默认为游客
        user.setRole(Const.Role.ROLE_CUSTOMER);

        returnCount = userMapper.insert(user);

        if (returnCount == 0) {
            return ServerResponse.createByErrorMsg("注册失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        int returnCount = 0;
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                returnCount = userMapper.selectByUsername(str);
                if (returnCount > 0) {
                    return ServerResponse.createByErrorMsg("账号已经注册");
                }
            }

            if (Const.EMAIL.equals(type)) {
                returnCount = userMapper.selectByEmail(str);
                if (returnCount > 0) {
                    return ServerResponse.createByErrorMsg("Email已经存在");
                }
            }

            if (Const.PHONE.equals(type)) {
                returnCount = userMapper.selectByPhone(str);
                if (returnCount > 0) {
                    return ServerResponse.createByErrorMsg("该手机号已经注册");
                }
            }
        } else {
            return ServerResponse.createByErrorMsg("参数错误");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> searchQuestion(String username) throws Exception {
        String question = null;
        ServerResponse<String> response = null;
        response = this.checkValid(username, Const.USERNAME);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMsg("用户不存在");
        }
        question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMsg("找回密码的用户问题不存在");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        User returnResult = userMapper.selectAnswer(username, question, answer);
        if (!Objects.equal(returnResult,null)) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PROFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMsg("问题的答案错误");
    }


    @Override
    public ServerResponse<String> resetPassword(String username, String passwordNew, String forgetToken) {
        ServerResponse<String> response = null;
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMsg("参数错误,需要传参token");
        }
        response = this.checkValid(username, Const.USERNAME);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMsg("用户不存在");
        }
        String passwordToken = TokenCache.getValue(TokenCache.TOKEN_PROFIX + username);

        if (StringUtils.isBlank(passwordToken)) {
            return ServerResponse.createByErrorMsg("token无效或者过期");
        }

        if (StringUtils.equals(forgetToken, passwordToken)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int count = userMapper.updatePasswordByUsername(username, md5Password);
            if (count > 0) {
                return ServerResponse.createBySuccess("密码修改成功");
            }
        } else {
            return ServerResponse.createByErrorMsg("token错误，请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMsg("密码设置失败，请重新获取token信息");
    }

    @Override
    public ServerResponse<String> loginResetPassword(String passwordOld, String passwordNew, User user) {
        String md5OldPassword = MD5Util.MD5EncodeUtf8(passwordOld);
        String md5NewPassword = MD5Util.MD5EncodeUtf8(passwordNew);
        int returnCount = userMapper.selectPasswordById(md5OldPassword, user.getId());
        if (returnCount == 0) {
            return ServerResponse.createByErrorMsg("该账号旧密码不正确");
        }
        user.setPassword(md5NewPassword);
        returnCount = userMapper.updateByPrimaryKeySelective(user);
        if (returnCount > 0) {
            return ServerResponse.createBySuccess("密码重置成功");
        }
        return ServerResponse.createByErrorMsg("密码重置失败");
    }

    @Override
    public ServerResponse<User> updateUserInfo(User user) {
        String email = user.getEmail();
        Integer userId = user.getId();
        Integer role = user.getRole();
//        String answer = user.getAnswer();
        String phone = user.getPhone();
//        查询用户的Email是否被占用，如果占用则输入新的邮箱
        int returnCount = userMapper.selectEmailById(email, userId);
        if (returnCount > 0){
            return ServerResponse.createByErrorMsg("该邮箱已经被占用，请重新输入");
        }
//        重新封装用户信息
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setEmail(email);
        updateUser.setRole(role);
        updateUser.setPhone(phone);

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新用户信息成功",updateUser);
        }
        return ServerResponse.createByErrorMsg("用户信息更新失败");
    }

    @Override
    public ServerResponse<User> getUserInfo(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null){
            return ServerResponse.createByErrorMsg("当前用户未登录");
        }
//        密码制为空
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse<String> checkRole(User user) {
        if (!Objects.equal(user,null) && user.getRole() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess("管理员登录",user.getUsername());
        }
        return ServerResponse.createByError();
    }

}
