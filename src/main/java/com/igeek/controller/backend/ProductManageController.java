package com.igeek.controller.backend;

import com.google.common.collect.Maps;
import com.igeek.common.Const;
import com.igeek.common.ResponseCode;
import com.igeek.common.ServerResponse;
import com.igeek.pojo.Product;
import com.igeek.pojo.User;
import com.igeek.service.portal.IUserService;
import com.igeek.service.system.IFileService;
import com.igeek.service.system.IProductService;
import com.igeek.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by Gyges on 2017/6/6.
 */
@Controller
@RequestMapping(value = "/manage/product/")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    /**
     * 新增或更新产品
     *
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse saveOrUpdateProduct(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), "需要登录");
        }
//        校验是否有管理员权限
        if (iUserService.checkRole(user).isSuccess()) {
            return iProductService.updateOrSaveProduct(product);
        } else {
            return ServerResponse.createByErrorMsg("您无权操作，需要管理员权限");
        }
    }


    /**
     * 更新产品状态
     *
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping(value = "set_status", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse setSalesProduct(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), "需要登录");
        }
//        校验是否有管理员权限
        if (iUserService.checkRole(user).isSuccess()) {
            return iProductService.setSaleStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMsg("您无权操作，需要管理员权限");
        }
    }

    /**
     * 获取产品详情
     *
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "get_detail", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProductDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), "需要登录");
        }
//        校验是否有管理员权限
        if (iUserService.checkRole(user).isSuccess()) {
            return iProductService.getProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMsg("您无权操作，需要管理员权限");
        }
    }

    /**
     * 获取产品列表
     *
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "get_list", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProductList(HttpSession session,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), "需要管理员登录");
        }
//        校验是否有管理员权限
        if (iUserService.checkRole(user).isSuccess()) {
            return iProductService.getProductList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMsg("您无权操作，需要管理员权限");
        }
    }

    /**
     * 产品搜索
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "search_list", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse searchProductList(HttpSession session,
                                            @RequestParam(value = "productName",required = false) String productName,
                                            @RequestParam(value = "productId",required = false) Integer productId,
                                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), "需要管理员登录");
        }
//        校验是否有管理员权限
        if (iUserService.checkRole(user).isSuccess()) {
            return iProductService.searchProductList(productName,productId,pageNum,pageSize);
        } else {
            return ServerResponse.createByErrorMsg("您无权操作，需要管理员权限");
        }
    }

    /**
     * 上传文件
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(HttpSession session,
                                 @RequestParam(value = "upload_file",required = false) MultipartFile file,
                                 HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeAndMsg(ResponseCode.NEED_LOGIN.getCode(), "需要管理员登录");
        }
//        校验是否有管理员权限
        if (iUserService.checkRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.uploadFile(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            Map<String,String> fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerResponse.createBySuccess(fileMap);
        } else {
            return ServerResponse.createByErrorMsg("您无权操作，需要管理员权限");
        }
    }

    /**
     * 富文本文件上传
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "rich_text_img_upload", method = RequestMethod.POST)
    @ResponseBody
    public Map richTextImgUpload(HttpSession session,
                                 @RequestParam(value = "upload_file",required = false) MultipartFile file,
                                 HttpServletRequest request) {
        Map<String,Object> map = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            map.put("success",false);
            map.put("msg","需要管理员登录");
            return  map;
        }
//        校验是否有管理员权限
        if (iUserService.checkRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.uploadFile(file,path);
            if (StringUtils.isBlank(targetFileName)){
                map.put("success",false);
                map.put("msg","上传失败");
                return map;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            map.put("success",true);
            map.put("msg","上传成功");
            map.put("file_path",url);
            return map;
        } else {
            map.put("success",false);
            map.put("msg","您无权操作");
            return  map;
        }
    }

}
