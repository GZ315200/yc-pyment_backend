package com.douzuiwa.service;

import com.douzuiwa.common.ServerResponse;
import com.douzuiwa.pojo.User;

/**
 * Created by Gyges on 2017/5/29.
 * 用户模块
 */
public interface IUserService {
    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public ServerResponse<User> login(String username, String password) throws Exception;

}
