package com.mmall.services;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;
import java.util.Set;

/**
 * Created by Liuzy on 2017/6/15.
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);
    ServerResponse updateCategory(String categoryName, Integer categoryId);
    ServerResponse<List<Category>> getSubCategoriesById(int categoryId);
    ServerResponse<Set<Category>> getCategoryAndSub(int categoryId);
}
