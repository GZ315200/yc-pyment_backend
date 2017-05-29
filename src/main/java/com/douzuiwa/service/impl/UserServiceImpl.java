package com.douzuiwa.service.impl;

import com.douzuiwa.common.ServerResponse;
import com.douzuiwa.dao.UserMapper;
import com.douzuiwa.pojo.User;
import com.douzuiwa.service.IUserService;
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

        User user = userMapper.selectLogin(username, password);
        if (user == null) {
            return ServerResponse.createByErrorMsg("登录密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }
}
