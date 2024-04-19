package com.example.minispring.service.Implementation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.minispring.controller.AuthController;
import com.example.minispring.model.AppUser;
import com.example.minispring.model.Opts;
import com.example.minispring.repository.AppUserRepository;
import com.example.minispring.repository.OptsRepository;
import com.example.minispring.service.OptsService;
@Service
public class OptsServiceImp implements OptsService {
    private OptsRepository optsRepository;
    private AppUserRepository appUserRepository;

    public OptsServiceImp(OptsRepository optsRepository,AppUserRepository appUserRepository) {
        this.optsRepository = optsRepository;
        this.appUserRepository = appUserRepository;
    }
    @Override
    public void insertOpt(String optCode, Boolean verify, Integer userId) {
        LocalDateTime issued = LocalDateTime.now();
        LocalDateTime expiration = issued.plus(1, ChronoUnit.MINUTES);
        optsRepository.insertOpt(optCode,issued, expiration, verify, userId);
    }
    @Override
    public String confirmOptCode(String optCode) {
        Opts opt = optsRepository.confirmOptCode(optCode);
        System.out.println(opt);
        if(opt == null){
            return "wrong";
        }
        if(opt.getExpiredAt().isBefore(LocalDateTime.now())){
            return "expired";
        }
        if(opt.getVerify()==false){
            optsRepository.confirmVerifyOptCode(optCode);
        }
        return "success";
    }

    @Override
    public Opts deleteOptCode(String email){
        AppUser userId = appUserRepository.findByEmail(email);
        Opts opt = optsRepository.deleteOptCode(userId.getId());
        return opt;
    }
}
