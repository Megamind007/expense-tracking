package com.example.minispring.service;

import java.util.List;

import com.example.minispring.model.Category;
import com.example.minispring.model.Request.CategoryRequest;

public interface CategoryService {
    List<Category> getAllCategory(String email,Integer offset,Integer limit);
    Category getCategoryById(String email, Integer categoryId);
    Category addNewCategory(CategoryRequest categoryRequest);
    Category updateCategoryById(CategoryRequest categoryRequest,Integer categoryId);
    Category deleteCategoryById(Integer categoryId);
}
