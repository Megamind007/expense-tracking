package com.example.minispring.model;

import java.time.LocalDateTime;

import com.example.minispring.model.Response.AppUserResponse;
import com.example.minispring.model.Response.CategoryResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Expense {
    private Integer expenseId;
    private double amount;
    private String description;
    private LocalDateTime date;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AppUserResponse userId;
    private CategoryResponse categoryId;
}
