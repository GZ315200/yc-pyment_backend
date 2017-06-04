package com.douzuiwa.service.system.impl;

import com.douzuiwa.common.ServerResponse;
import com.douzuiwa.dao.CategoryMapper;
import com.douzuiwa.pojo.Category;
import com.douzuiwa.service.system.ICategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Gyges on 2017/6/4.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if (StringUtils.isBlank(categoryName) || parentId == null){
            return ServerResponse.createByErrorMsg("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0){
            return  ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMsg("添加品类失败");
    }

    @Override
    public ServerResponse<String> updateCategoryName(String categoryName, Integer categoryId) {
        if (StringUtils.isBlank(categoryName) || categoryId == null){
            return ServerResponse.createByErrorMsg("品类信息错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0){
            return  ServerResponse.createBySuccess("更新品类信息成功");
        }
        return ServerResponse.createByErrorMsg("更新品类信息失败");
    }


}
