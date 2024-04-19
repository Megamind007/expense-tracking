package com.example.minispring.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.minispring.model.Expense;
import com.example.minispring.model.ApiResponse.ApiResponse;
import com.example.minispring.model.Request.ExpenseRequest;
import com.example.minispring.service.ExpenseService;

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
@RequestMapping("/api/v1/expenses")
@SecurityRequirement(name="bearerAuth")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Expense>>> getAllExpense(
        @RequestParam(defaultValue = "1") @Positive Integer offset,@RequestParam(defaultValue = "3") @Positive Integer limit,@RequestParam(defaultValue = "expense_id") String sortBy,@RequestParam(defaultValue = "false") boolean orderBy) {
        List<Expense> expenses = expenseService.getAllExpense(offset, limit,sortBy,orderBy);
        ApiResponse<List<Expense>> response = ApiResponse.<List<Expense>>builder()
            .message(expenses!=null?"Get All Expense Successfully":"No Data Available")
            .payload(expenses)
            .httpStatus(HttpStatus.OK)
            .localDateTime(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Expense>> getExpenseById(@PathVariable @Positive Integer id){
        Expense expense = expenseService.getExpenseById(id);
        ApiResponse<Expense> response = ApiResponse.<Expense>builder()
            .message(expense!=null?"Expense with Id "+id+" is founded":"No Data Available")
            .payload(expense)
            .httpStatus(HttpStatus.OK)
            .localDateTime(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Expense>> addNewExpense(@RequestBody @Valid ExpenseRequest expenseRequest){
        Expense expense = expenseService.addNewExpense(expenseRequest);
        ApiResponse<Expense> response = ApiResponse.<Expense>builder()
            .message(expense!=null?"New Expense has been add Successfully":"No Data Available")
            .payload(expense)
            .httpStatus(HttpStatus.OK)
            .localDateTime(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Expense>> updateExpenseById(@RequestBody @Valid ExpenseRequest expenseRequest,@PathVariable @Positive Integer id){
        Expense expense = expenseService.updateExpense(expenseRequest,id);
        ApiResponse<Expense> response = ApiResponse.<Expense>builder()
            .message(expense!=null?"Expense with id "+id+" has been update Successfully":"No Data Available")
            .payload(expense)
            .httpStatus(HttpStatus.OK)
            .localDateTime(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Expense>> deleteExpense(@PathVariable @Positive Integer id){
        Expense expense = expenseService.deleteExpense(id);
        ApiResponse<Expense> response = ApiResponse.<Expense>builder()
            .message(expense!=null?"Category with id "+id+" has been delete Successfully.":"No Data Available")
            .payload(null)
            .httpStatus(HttpStatus.OK)
            .localDateTime(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
