package com.igeek.service.system.impl;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.igeek.common.ServerResponse;
import com.igeek.dao.CategoryMapper;
import com.igeek.pojo.Category;
import com.igeek.service.system.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

/**
 * Created by Gyges on 2017/6/4.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger Logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if (StringUtils.isBlank(categoryName) || parentId == null) {
            return ServerResponse.createByErrorMsg("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMsg("添加品类失败");
    }

    @Override
    public ServerResponse<String> updateCategoryName(String categoryName, Integer categoryId) {
        if (StringUtils.isBlank(categoryName) || categoryId == null) {
            return ServerResponse.createByErrorMsg("品类信息错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新品类信息成功");
        }
        return ServerResponse.createByErrorMsg("更新品类信息失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId) {
        List<Category> categoryList = categoryMapper.getChildrenIdByParentId(parentId);
        if (CollectionUtils.isEmpty(categoryList)) {
            String msg = MessageFormat.format("获取子目录失败 {0}", parentId);
            Logger.error(msg);
        }
        return ServerResponse.createBySuccess("获取子目录成功", categoryList);
    }

    /**
     * 递归查询此节点的id和孩子节点的id
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<List<Integer>> getCategoryAndDeepChildrenCategory(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        findChildrenItem(categorySet,categoryId);

        List<Integer> categoryList = Lists.newArrayList();
        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 递归算法，算出子节点,递归要有退出条件
     * @param categorySet
     * @param categoryId
     * @return
     */
    private Set<Category> findChildrenItem(Set<Category> categorySet ,Integer categoryId){
//        查找该节点下的所有节点，不重复
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (!Objects.equal(category,null)){
            categorySet.add(category);
        }
//        通过父节点查询子节点
        List<Category> categoryList = categoryMapper.getChildrenIdByParentId(categoryId);
        for (Category categoryItem : categoryList){
            findChildrenItem(categorySet,categoryItem.getId());
        }
        return categorySet;
    }


}
