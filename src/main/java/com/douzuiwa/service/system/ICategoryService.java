package com.douzuiwa.service.system;

import com.douzuiwa.common.ServerResponse;

/**
 * Created by Gyges on 2017/6/4.
 */
public interface ICategoryService {

    public ServerResponse<String> addCategory(String categoryName,Integer parentId);

    public ServerResponse<String> updateCategoryName(String categoryName,Integer categoryId);
}
