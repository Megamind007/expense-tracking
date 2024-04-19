package com.example.minispring.service.Implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.minispring.controller.AuthController;
import com.example.minispring.model.AppUser;
import com.example.minispring.model.Category;
import com.example.minispring.model.Request.CategoryRequest;
import com.example.minispring.repository.AppUserRepository;
import com.example.minispring.repository.CategoryRepository;
import com.example.minispring.service.CategoryService;
import com.example.minispring.validation.NotFound;
@Service
public class CategoryServiceImp implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final AppUserRepository appUserRepository;
    public CategoryServiceImp(CategoryRepository categoryRepository,AppUserRepository appUserRepository) {
        this.categoryRepository = categoryRepository;
        this.appUserRepository = appUserRepository;
    }
    @Override
    public List<Category> getAllCategory(String email,Integer offset,Integer limit) {
        offset = (offset-1) * limit;
        List<Category> category = categoryRepository.getAllCategory(email,offset, limit);
        if(category.isEmpty()){
            return null;
        }
        return category;
    }
    @Override
    public Category getCategoryById(String email, Integer categoryId){
        Category category = categoryRepository.getCategoryById(email,categoryId);
        if(category == null){
            throw new NotFound("Category with ID " +categoryId +" not found");
        }
        return category;
    }
    @Override
    public Category addNewCategory(CategoryRequest categoryRequest){
        String email = AuthController.getUsernameOfCurrentUser();
        AppUser userId = appUserRepository.findByEmail(email);
        Category category = categoryRepository.addNewCategory(categoryRequest,userId.getId()); 
        System.out.println(category);
        return category;
    }

    @Override
    public Category updateCategoryById(CategoryRequest categoryRequest,Integer categoryId){
        String email = AuthController.getUsernameOfCurrentUser();
        AppUser userId = appUserRepository.findByEmail(email);
        Category category = categoryRepository.updateCategoryById(categoryRequest, categoryId, userId.getId());
        if(category==null){
            throw new NotFound("Category with ID " +categoryId +" not found");
        }
        return category;
    }

    @Override
    public Category deleteCategoryById(Integer categoryId){
        String email = AuthController.getUsernameOfCurrentUser();
        AppUser userId = appUserRepository.findByEmail(email);
        Category category = categoryRepository.deleteCategoryById(categoryId, userId.getId());
        if(category==null){
            throw new NotFound("Category with ID " +categoryId +" not found");
        }
        return category;
    }
}
