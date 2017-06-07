package com.igeek.service.system;

import com.igeek.common.ServerResponse;
import com.igeek.pojo.Category;

import java.util.List;

/**
 * Created by Gyges on 2017/6/4.
 */
public interface ICategoryService {

    public ServerResponse<String> addCategory(String categoryName,Integer parentId);

    public ServerResponse<String> updateCategoryName(String categoryName,Integer categoryId);

    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId);

    public ServerResponse<List<Integer>> getCategoryAndDeepChildrenCategory(Integer categoryId);
}
