package com.douzuiwa.service.system.impl;

import com.douzuiwa.common.ServerResponse;
import com.douzuiwa.common.TokenCache;
import com.douzuiwa.dao.UserMapper;
import com.douzuiwa.pojo.User;
import com.douzuiwa.service.system.IUserAdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Gyges on 2017/5/30.
 * 用于后台用户登录
 */
@Service
public class UserAdminService implements IUserAdminService {

    @Resource
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> adminLogin(String phone) {
        if (StringUtils.isBlank(phone)){
            return ServerResponse.createByErrorMsg("手机号不为空");
        }
        User user = userMapper.selectAdminLogin(phone);
        if (user == null) {
            return ServerResponse.createByErrorMsg("该用户未注册，请点击注册");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("手机号可用于登录",user);
    }

    @Override
    public ServerResponse<String> getMessageCode(String phone) {
        String messageCode = null;
        if (StringUtils.isBlank(phone)){
            return ServerResponse.createByErrorMsg("手机号码必填");
        }else{
            messageCode = "123456";
            TokenCache.setKey("MESSAGE_"+phone,messageCode);
        }
        return ServerResponse.createBySuccess("获得短信验证码成功",messageCode);
    }


}
