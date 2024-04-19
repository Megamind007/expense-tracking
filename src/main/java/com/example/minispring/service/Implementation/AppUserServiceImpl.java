package com.example.minispring.service.Implementation;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.minispring.model.AppUser;
import com.example.minispring.model.Opts;
import com.example.minispring.model.Request.AppUserRequest;
import com.example.minispring.model.Request.AppUserRequestPassword;
import com.example.minispring.model.Response.AppUserResponse;
import com.example.minispring.repository.AppUserRepository;
import com.example.minispring.repository.OptsRepository;
import com.example.minispring.service.AppUserService;
import com.example.minispring.validation.NotFound;

@Service
public class AppUserServiceImpl implements AppUserService{
   private final AppUserRepository appUserRepository;
   private final OptsRepository optsRepository;
   public AppUserServiceImpl(AppUserRepository appUserRepository,OptsRepository optsRepository) {
       this.appUserRepository = appUserRepository;
       this.optsRepository = optsRepository;
   }
   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    AppUser appUser = appUserRepository.findByEmail(email);
    if(appUser == null){
        throw new NotFound("Email not found");
    }
    Opts opt = optsRepository.checkUserId(appUser.getId());
    if(opt.getVerify() == false) {
        throw new NotFound("Your account has not been verified");
    }
    return appUser;
   }

   @Override
   public UserDetails resendOtpCheckMail(String email) throws UsernameNotFoundException {
    AppUser appUser = appUserRepository.findByEmail(email);
    System.out.println(appUser);
    if(appUser == null){
        throw new NotFound("Email not found");
    }
    return appUser;
   }
   @Override
   public void saveUser(AppUserRequest appUserRequest){
        AppUser user = appUserRepository.findByEmail(appUserRequest.getEmail());
        System.out.println(user);
        if(user !=null){
            throw new NotFound("Email already exists");
        }
        appUserRepository.saveUser(appUserRequest);
   }

   @Override
   public void changePassword(String appUserRequestPassword,String email){
        AppUser appUser = appUserRepository.findByEmail(email);
        appUserRepository.changePassword(appUserRequestPassword, appUser.getId());
   }
}
