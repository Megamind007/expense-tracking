package com.example.minispring.service.Implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.minispring.controller.AuthController;
import com.example.minispring.model.AppUser;
import com.example.minispring.model.Category;
import com.example.minispring.model.Expense;
import com.example.minispring.model.Request.ExpenseRequest;
import com.example.minispring.repository.AppUserRepository;
import com.example.minispring.repository.CategoryRepository;
import com.example.minispring.repository.ExpenseRepository;
import com.example.minispring.service.ExpenseService;
import com.example.minispring.validation.NotFound;
@Service
public class ExpenseServiceImp implements ExpenseService{
    private ExpenseRepository expenseRepository;
    private AppUserRepository appUserRepository;
    private CategoryRepository categoryRepository;
    public ExpenseServiceImp(ExpenseRepository expenseRepository,AppUserRepository appUserRepository,CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.appUserRepository = appUserRepository;
        this.categoryRepository=categoryRepository;
    }

    @Override
    public List<Expense> getAllExpense(Integer offset,Integer limit,String sortBy,boolean orderBy) {
        System.out.println(sortBy);
        System.out.println(orderBy);
        offset = (offset-1) * limit;
        String order = "";
        if(orderBy==true) {
            order = "DESC";
        }else{
            order="ASC";
        }
        String email = AuthController.getUsernameOfCurrentUser();
        AppUser userId = appUserRepository.findByEmail(email);
        System.out.println(sortBy+order);
        List<Expense> expense = expenseRepository.getAllExpense(userId.getId(), offset, limit,sortBy,order);
        if(expense.isEmpty()){
            return null;
        }
        return expense;
    }

    @Override
    public Expense getExpenseById(Integer expenseId){
        String email = AuthController.getUsernameOfCurrentUser();
        AppUser userId = appUserRepository.findByEmail(email);
        Expense expense = expenseRepository.getExpenseById(expenseId,userId.getId());
        if(expense == null){
            throw new NotFound("Expense with id " + expenseId + " not found");
        }
        return expense;
    }

    @Override
    public Expense addNewExpense(ExpenseRequest expenseRequest){
        boolean check = true;
        String email = AuthController.getUsernameOfCurrentUser();
        AppUser userId = appUserRepository.findByEmail(email);
        Category category = categoryRepository.getCategoryById(email, expenseRequest.getCategoryId());
        System.out.println(category);
        if(category == null){
            check = false;
            throw new NotFound("Category with ID " +expenseRequest.getCategoryId() +" not found");
        }
        Expense expense = null;
        if(check==true){
            expense = expenseRepository.addNewExpense(expenseRequest,userId.getId());
            if(expense == null){
                return null;
            }
        }
        return expense;
    }

    @Override
    public Expense updateExpense(ExpenseRequest expenseRequest,Integer id){
        boolean check = true;
        String email = AuthController.getUsernameOfCurrentUser();
        AppUser userId = appUserRepository.findByEmail(email);
        Category category = categoryRepository.getCategoryById(email, expenseRequest.getCategoryId());
        Expense expense = expenseRepository.getExpenseById(id, userId.getId());
        System.out.println(category);
        System.out.println(expense);
        if(expense==null){
            check = false;
            throw new NotFound("Expense with ID " +id +" not found");
        }
        if(category==null){
            check = false;
            throw new NotFound("Category with ID " +expenseRequest.getCategoryId() +" not found");
        }
        if(check==true){
            expense = expenseRepository.updateExpense(expenseRequest,id,userId.getId());
            if(expense == null){
                return null;
            }
        }
        return expense;
    }

    @Override
    public Expense deleteExpense(Integer expenseId){
        String email = AuthController.getUsernameOfCurrentUser();
        AppUser userId = appUserRepository.findByEmail(email);
        Expense expense = expenseRepository.deleteExpense(expenseId,userId.getId());
        if(expense == null){
            throw new NotFound("Expense with Id " + expenseId + " not found");
        }
        return expense;
    }
}
