package com.example.minispring.service;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;

import com.example.minispring.model.Opts;

public interface OptsService {
    void insertOpt(String optCode, Boolean verify, Integer userId);
    String confirmOptCode(String optCode);
    Opts deleteOptCode(String email);
}
