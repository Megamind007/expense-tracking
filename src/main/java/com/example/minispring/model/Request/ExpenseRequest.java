package com.example.minispring.model.Request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseRequest {
    @Positive
    @NotNull
    private double amount;
    @NotBlank
    @NotNull
    private String description;
    private LocalDateTime date;
    @Positive
    @NotNull
    private Integer categoryId;
}
