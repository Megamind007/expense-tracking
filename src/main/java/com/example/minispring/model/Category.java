package com.example.minispring.model;

import com.example.minispring.model.Response.AppUserResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private Integer  categoryId;
    private String categoryName;
    private String categoryDescription;
    private AppUserResponse userId;
}
