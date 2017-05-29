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

    /**
     * 注册用户
     * @param user
     * @return
     * @throws Exception
     */
    public ServerResponse<String> register(User user) throws Exception;

    /**
     * 验证用户信息
     * @param str
     * @param type
     * @return
     * @throws Exception
     */
    public ServerResponse<String> checkValid(String str,String type) throws Exception;

}
