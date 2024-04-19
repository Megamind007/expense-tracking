package com.example.minispring.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.minispring.model.Request.AppUserRequest;
import com.example.minispring.model.Request.AppUserRequestPassword;

public interface AppUserService extends UserDetailsService {
    void saveUser(AppUserRequest appUserRequest);
    UserDetails resendOtpCheckMail(String email) throws UsernameNotFoundException;
    void changePassword(String appUserRequestPassword,String email);
}