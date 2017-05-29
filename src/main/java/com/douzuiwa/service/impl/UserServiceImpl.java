package com.douzuiwa.service.impl;

import com.douzuiwa.common.Const;
import com.douzuiwa.common.ServerResponse;
import com.douzuiwa.dao.UserMapper;
import com.douzuiwa.pojo.User;
import com.douzuiwa.service.IUserService;
import com.douzuiwa.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Logger.info("登录的账号数: "+userCount);
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
        response = this.checkValid(user.getUsername(),Const.USERNAME);
        if (!response.isSuccess()){
            return response;
        }

//        设置密码
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        // TODO: 2017/5/30  检验email
        response = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!response.isSuccess()){
            return response;
        }
// TODO: 2017/5/30 检验phone
        response =this.checkValid(user.getPhone(),Const.PHONE);
        if(!response.isSuccess()){
            return response;
        }

//        设置角色 默认为游客
        user.setRole(Const.Role.ROLE_CUSTOMER);

        returnCount = userMapper.insert(user);

        if (returnCount == 0){
            return ServerResponse.createByErrorMsg("注册失败");
        }
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> checkValid(String str,String type){
        int returnCount = 0;
        if (StringUtils.isNotBlank(type)){
            if (Const.USERNAME.equals(type)){
                returnCount = userMapper.selectByUsername(str);
                if (returnCount > 0) {
                    return ServerResponse.createByErrorMsg("账号已经注册");
                }
            }

            if (Const.EMAIL.equals(type)){
                returnCount = userMapper.selectByEmail(str);
                if(returnCount > 0){
                    return ServerResponse.createByErrorMsg("Email已经存在");
                }
            }

            if (Const.PHONE.equals(type)){
                returnCount = userMapper.selectByPhone(str);
                if (returnCount > 0){
                    return ServerResponse.createByErrorMsg("该手机号已经注册");
                }
            }
        }else {
            return ServerResponse.createByErrorMsg("参数错误");
        }
        return ServerResponse.createBySuccess();
    }
}
