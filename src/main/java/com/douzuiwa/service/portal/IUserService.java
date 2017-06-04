package com.douzuiwa.service.portal;

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
     * 验证用户信息(检验注册信息存不存在)
     * @param str
     * @param type
     * @return  注册信息不存在
     * @throws Exception
     */
    public ServerResponse<String> checkValid(String str,String type) throws Exception;

    /**
     *查询用户找回密码问题
     * @param username
     * @return
     */
    public ServerResponse<String> searchQuestion(String username) throws Exception;

    /**
     * 检查问题
     * @param username
     * @param question
     * @param answer
     * @return
     */
    public ServerResponse<String> checkAnswer(String username,String question,String answer);

    /**
     * 重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    public ServerResponse<String> resetPassword(String username,String passwordNew,String forgetToken);

    /**
     * 登录时修改密码
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    public ServerResponse<String> loginResetPassword(String passwordOld,String passwordNew,User user);

    /**
     * 更新个人信息
     * @param user
     * @return
     */
    public ServerResponse<User> updateUserInfo(User user);

    /**
     * 获取用户详情
     * @param userId
     * @return
     */
    public ServerResponse<User> getUserInfo(Integer userId);

    public ServerResponse<String> checkRole(User user);
}
