package com.mmall.services.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.services.ICategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Liuzy on 2017/6/15.
 */
@Service("iCategoryService")
public class CategoryServiceimpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceimpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse addCategory(String categoryName, Integer parentId){

        if (parentId == null || org.apache.commons.lang3.StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorErrorMessage("参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0){
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorErrorMessage("失败添加品类");
    }

    public ServerResponse updateCategory(String categoryName, Integer categoryId){

        if (categoryId == null || org.apache.commons.lang3.StringUtils.isBlank(categoryName) || categoryName == null){
            return ServerResponse.createByErrorErrorMessage("参数不能为空");
        }else {
            Category category = new Category();
            category.setId(categoryId);
            category.setName(categoryName);
            int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("品类更名成功");
            } else {
                return ServerResponse.createByErrorErrorMessage("更新失败");
            }
        }
    }

    public ServerResponse<List<Category>> getSubCategoriesById(int categoryId){

        List<Category> categoryList = categoryMapper.getSubCategoriesById(categoryId);
        if (CollectionUtils.isEmpty(categoryList)){
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    public ServerResponse<Set<Category>> getCategoryAndSub(int categoryId){

        Set<Category> categorySet = new HashSet<Category>();
        this.findCategories(categorySet,categoryId);

        return ServerResponse.createBySuccess(categorySet);
    }

    private Set<Category> findCategories(Set<Category> set,int categoryId){

        Category category = categoryMapper.selectByPrimaryKey(categoryId);

        if (category != null){
            set.add(category);
        }

        List<Category> list = categoryMapper.getSubCategoriesById(categoryId);
        for (Category item:list) {
            findCategories(set,item.getId());
        }
        return set;
    }

}
