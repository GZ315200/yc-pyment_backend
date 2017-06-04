package com.douzuiwa.controller.backend;

import com.douzuiwa.common.Const;
import com.douzuiwa.common.ResponseCode;
import com.douzuiwa.common.ServerResponse;
import com.douzuiwa.pojo.User;
import com.douzuiwa.service.portal.IUserService;
import com.douzuiwa.service.system.ICategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by Gyges on 2017/6/3.
 */
@Controller
@RequestMapping(value = "/manage/category/")
public class CategoryManageController {

    @Resource
    private IUserService iUserService;

    @Resource
    private ICategoryService iCategoryService;

    /**
     * 增加品类信息
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping(value = "add_category",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addCategory(HttpSession session,String categoryName,@RequestParam(defaultValue = "0",value = "parentId") int parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),"需要登录");
        }
//        校验是否有管理员权限
        if (iUserService.checkRole(user).isSuccess()){
            return iCategoryService.addCategory(categoryName,parentId);
        }else{
            return ServerResponse.createByErrorMsg("您无权操作，需要管理员权限");
        }
    }

    /**
     * 更新品类信息
     * @param session
     * @param categoryName
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "set_category_name",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> setCategoryName(HttpSession session,String categoryName,Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),"需要登录");
        }
        if (iUserService.checkRole(user).isSuccess()){
            return iCategoryService.updateCategoryName(categoryName, categoryId);
        }else{
            return ServerResponse.createByErrorMsg("您无权操作，需要管理员权限");
        }
    }
}
