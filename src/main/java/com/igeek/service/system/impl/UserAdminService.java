package com.igeek.service.system.impl;

import com.igeek.common.ServerResponse;
import com.igeek.dao.UserMapper;
import com.igeek.pojo.User;
import com.igeek.service.system.IUserAdminService;
import com.igeek.util.OkHttpUtils;
import com.igeek.util.PropertiesUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gyges on 2017/5/30.
 * 用于后台用户登录
 */
@Service("userAdminService")
public class UserAdminService implements IUserAdminService {

    @Resource
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> adminLogin(String phone,String messageCode) throws IOException {
        String authUrl = PropertiesUtil.getProperty("message.auth.url");
        Map<String,String> messageMap = new HashMap<String,String>();
        if (StringUtils.isBlank(phone)){
            return ServerResponse.createByErrorMsg("手机号不为空");
        }
        User user = userMapper.selectAdminLogin(phone);
        if (user == null) {
            return ServerResponse.createByErrorMsg("该用户未注册，请点击注册");
        }
        messageMap.put("mobile",phone);
        messageMap.put("code",messageCode);
//        获取用户的messageCode
        String messageJson = OkHttpUtils.post(authUrl,messageMap);
        JsonObject jsonObject = new JsonParser().parse(messageJson).getAsJsonObject();
        String status = jsonObject.get("code").getAsString();
        if(status.equals("200")){
            user.setPassword(StringUtils.EMPTY);
            return ServerResponse.createBySuccess("登录成功",user);
        }
        return ServerResponse.createBySuccess("验证码不正确,请重新登录");
    }

    @Override
    public ServerResponse<String> getMessageCode(String mobile) throws IOException {
        String messageUrl = PropertiesUtil.getProperty("message.send.url");
        String messageCode = null;
        String messageJson = null;
        Map<String,String> messageMap = new HashMap<String,String>();
        if (StringUtils.isBlank(mobile)){
            return ServerResponse.createByErrorMsg("手机号码必填");
        }else{
//            todo {1.调用短信接口获取验证码，用户输入验证码验证是否一致，如果一致则登录成功}
            User user = userMapper.selectAdminLogin(mobile);
            if (user == null) {
                return ServerResponse.createByErrorMsg("该用户未注册，请点击注册");
            }
            messageMap.put("mobile",mobile);
            messageJson = OkHttpUtils.post(messageUrl,messageMap);
            JsonObject jsonObject = new JsonParser().parse(messageJson).getAsJsonObject();
            String status = jsonObject.get("code").getAsString();
            if (status.equals("200")) {
                messageCode = jsonObject.get("obj").getAsString();
            }
//            TokenCache.setKey("MESSAGE_"+phone,messageCode);
        }
        return ServerResponse.createBySuccess("获得短信验证码成功",messageCode);
    }


}
