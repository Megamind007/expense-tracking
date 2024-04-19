package com.example.minispring.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.minispring.model.Category;
import com.example.minispring.model.ApiResponse.ApiResponse;
import com.example.minispring.model.Request.CategoryRequest;
import com.example.minispring.service.CategoryService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/categories")
@SecurityRequirement(name="bearerAuth")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategory(@RequestParam(defaultValue = "1") @Positive Integer offset,@RequestParam(defaultValue = "3") @Positive Integer limit) {
        List<Category> category = categoryService.getAllCategory(AuthController.getUsernameOfCurrentUser(),offset,limit);
        ApiResponse<List<Category>> response = ApiResponse.<List<Category>>builder()
            .message(category!=null?"Get All Categories Successfully":"No Data Available")
            .payload(category)
            .httpStatus(HttpStatus.OK)
            .localDateTime(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable @Positive Integer id) {
        Category category = categoryService.getCategoryById(AuthController.getUsernameOfCurrentUser(),id);
        ApiResponse<Category> response = ApiResponse.<Category>builder()
            .message(category!=null?"Category with Id " +id+ " is founded":"No Data Available")
            .payload(category)
            .httpStatus(HttpStatus.OK)
            .localDateTime(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> addNewCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        Category category = categoryService.addNewCategory(categoryRequest);
        ApiResponse<Category> response = ApiResponse.<Category>builder()
            .message(category!=null?"New Category has been add Successfully":"No Data Available")
            .payload(category)
            .httpStatus(HttpStatus.OK)
            .localDateTime(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategoryById(@RequestBody @Valid CategoryRequest categoryRequest, @PathVariable @Positive Integer id) {
        Category category = categoryService.updateCategoryById(categoryRequest,id);
        ApiResponse<Category> response = ApiResponse.<Category>builder()
            .message(category!=null?"Category with ID "+id+" has been update Successfully.":"No Data Available")
            .payload(category)
            .httpStatus(HttpStatus.OK)
            .localDateTime(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> deleteCategoryById(@PathVariable @Positive Integer id) {
        Category category = categoryService.deleteCategoryById(id);
        ApiResponse<Category> response = ApiResponse.<Category>builder()
            .message(category!=null?"Categories with ID "+id+" has been deleted Successfully":"No Data Available")
            .payload(null)
            .httpStatus(HttpStatus.OK)
            .localDateTime(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
