package com.igeek.controller.backend;

import com.igeek.common.Const;
import com.igeek.common.ResponseCode;
import com.igeek.common.ServerResponse;
import com.igeek.pojo.User;
import com.igeek.service.portal.IUserService;
import com.igeek.service.system.ICategoryService;
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
    @RequestMapping(value = "add_category",method = RequestMethod.GET)
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
    @RequestMapping(value = "set_category_name",method = RequestMethod.GET)
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


    /**
     * 查询子节点的category信息，并且不递归，保持平级
     * @param session
     * @param parentId 父节点
     * @return
     */
    @RequestMapping(value = "get_category",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "parentId",defaultValue = "0") Integer parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),"需要登录");
        }
        if (iUserService.checkRole(user).isSuccess()){
//            查询子节点的category信息，并且不递归，保持平级
            return  iCategoryService.getChildrenParallelCategory(parentId);
        }else{
            return ServerResponse.createByErrorMsg("您无权操作，需要管理员权限");
        }
    }


    /**
     * 查询当前子节点的id，和递归子节点的id
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "get_deep_category",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(),"需要登录");
        }
        if (iUserService.checkRole(user).isSuccess()){
            return iCategoryService.getCategoryAndDeepChildrenCategory(categoryId);
        }else{
            return ServerResponse.createByErrorMsg("您无权操作，需要管理员权限");
        }
    }



}
