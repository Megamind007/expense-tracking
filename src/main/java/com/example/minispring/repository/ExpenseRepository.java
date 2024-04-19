package com.example.minispring.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import com.example.minispring.model.Expense;
import com.example.minispring.model.Request.ExpenseRequest;

@Mapper
public interface ExpenseRepository {
    @Results(id = "ExpenseMapper",value = {
        @Result(property = "expenseId",column = "expense_id"),
        @Result(property = "categoryId",column = "category_id",one = @One(select = "com.example.minispring.repository.CategoryRepository.getCategoryByIdResponse")),
        @Result(property = "userId",column = "user_id",one = @One(select = "com.example.minispring.repository.AppUserRepository.findUserById"))
    })
    @Select("""
        SELECT * FROM expenses_tb
        WHERE user_id = #{userId}
        ORDER BY ${sortBy} ${orderBy}
        LIMIT #{limit} OFFSET #{offset}
    """)
    List<Expense> getAllExpense(Integer userId, Integer offset, Integer limit, String sortBy, String orderBy);
    

    @Select("""
            select * from expenses_tb WHERE expense_id = #{expenseId} AND user_id = #{userId};
    """)
    @ResultMap("ExpenseMapper")
    Expense getExpenseById(Integer expenseId,Integer userId);

    @Select("""
            insert into expenses_tb(amount,description,date,category_id,user_id) values (#{expense.amount}, #{expense.description}, #{expense.date}, #{expense.categoryId}, #{userId}) returning *;
    """)
    @ResultMap("ExpenseMapper")
    Expense addNewExpense(@Param("expense")ExpenseRequest expenseRequest,Integer userId);

    @Select("""
        update expenses_tb set amount = #{expense.amount}, description = #{expense.description}, date = #{expense.date}, category_id = #{expense.categoryId} where expense_id = #{expenseId} AND user_id = #{userId} returning *;
    """)
    @ResultMap("ExpenseMapper")
    Expense updateExpense(@Param("expense")ExpenseRequest expenseRequest,Integer expenseId,Integer userId);

    @Select("""
        delete from expenses_tb where expense_id = #{expenseId} AND user_id = #{userId} returning *; 
    """)
    Expense deleteExpense(Integer expenseId,Integer userId);
}
