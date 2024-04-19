package com.example.minispring.service;

import java.util.List;

import com.example.minispring.model.Expense;
import com.example.minispring.model.Request.ExpenseRequest;

public interface ExpenseService {
    List<Expense> getAllExpense(Integer offset,Integer limit,String sortBy,boolean orderBy);
    Expense getExpenseById(Integer expenseId);
    Expense addNewExpense(ExpenseRequest expenseRequest);
    Expense updateExpense(ExpenseRequest expenseRequest,Integer id);
    Expense deleteExpense(Integer expenseId);
}
