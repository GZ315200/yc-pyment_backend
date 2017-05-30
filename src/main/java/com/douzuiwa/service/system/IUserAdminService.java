package com.douzuiwa.service.system;

import com.douzuiwa.common.ServerResponse;
import com.douzuiwa.pojo.User;

/**
 * Created by Gyges on 2017/5/30.
 */
public interface IUserAdminService {

    public ServerResponse<User> adminLogin(String phone);

    public ServerResponse<String> getMessageCode(String phone);
}
