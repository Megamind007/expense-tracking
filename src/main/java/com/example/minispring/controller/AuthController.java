package com.example.minispring.controller;

import java.io.IOException;

import org.apache.coyote.BadRequestException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.minispring.jwt.JwtService;
import com.example.minispring.model.AppUser;
import com.example.minispring.model.Opts;
import com.example.minispring.model.Request.AppUserRequest;
import com.example.minispring.model.Request.AppUserRequestPassword;
import com.example.minispring.model.Request.AuthRequest;
import com.example.minispring.model.Response.AuthResponse;
import com.example.minispring.repository.AppUserRepository;
import com.example.minispring.repository.OptsRepository;
import com.example.minispring.service.AppUserService;
import com.example.minispring.service.EmailingService;
import com.example.minispring.service.FileService;
import com.example.minispring.service.OptsService;
import com.example.minispring.validation.NotFound;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AppUserService appUserService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final FileService fileService;
    private final EmailingService emailingService;
    private final AppUserRepository appUserRepository;
    private final OptsService optsService;
    private final OptsRepository optsRepository;

    public AuthController(AppUserService appUserService, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService,FileService fileService,EmailingService emailingService,AppUserRepository appUserRepository,OptsService optsService,OptsRepository optsRepository) {
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.fileService = fileService;
        this.emailingService = emailingService;
        this.appUserRepository=appUserRepository;
        this.optsService=optsService;
        this.optsRepository=optsRepository;
    }
    private void authenticate(String username, String password) throws Exception {
    try {
       UserDetails userApp = appUserService.loadUserByUsername(username);
       System.out.println(userApp);
       if (userApp == null){throw new BadRequestException("Wrong Email");}
       if (!passwordEncoder.matches(password, userApp.getPassword())){
           throw new NotFound("Wrong Password");}
       authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
       throw new Exception("USER_DISABLED", e);} catch (BadCredentialsException e) {
       throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthRequest authRequest) throws Exception {
       authenticate(authRequest.getEmail(), authRequest.getPassword());
       final UserDetails userDetails = appUserService.loadUserByUsername(authRequest.getEmail());
       final String token = jwtService.generateToken(userDetails);
       AuthResponse authResponse = new AuthResponse(token);
       return ResponseEntity.ok(authResponse);
    }


    public static String getUsernameOfCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                                    .getPrincipal();
        String username = userDetails.getUsername();
        return username;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid AppUserRequest appUserRequest){
        boolean check = true;
        if(!appUserRequest.getPassword().equals(appUserRequest.getConfirmPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match");
        }
        String encryptedPassword = passwordEncoder.encode(appUserRequest.getPassword());
        appUserRequest.setPassword(encryptedPassword);
        try {
            Resource resource = fileService.getFileByFileName(appUserRequest.getImage());
        }catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image Not found");
        }
        if(check){
            int randomNumber = (int) (Math.random() * 1000000);
            String formattedNumber = String.format("%06d", randomNumber);
            try {
                emailingService.sendMail(appUserRequest.getEmail(),"Verify", formattedNumber);
            } catch (MessagingException e) {
                System.out.println("Error sending email: " + e.getMessage());
            }
            appUserService.saveUser(appUserRequest);
            AppUser userId = appUserRepository.findByEmail(appUserRequest.getEmail());
            optsService.insertOpt(formattedNumber, false, userId.getId());
        }

        return ResponseEntity.status(HttpStatus.OK).body("Registration Successfully. Please verify your registration");
    }

    @PutMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam @Positive String optCode) {
        String result = optsService.confirmOptCode(optCode);
        return ResponseEntity.status(HttpStatus.OK).body(result.equals("wrong")?"Wrong OptCode":result.equals("expired")?"Code Expired":"Email Register Successfully.");
    }

    @PutMapping("resend")
    public ResponseEntity<?> resendOpts(@RequestParam @Email String email) {
        appUserService.resendOtpCheckMail(email);
        AppUser userId = appUserRepository.findByEmail(email);
        Opts opt = optsRepository.checkUserId(userId.getId());
        if(opt.getVerify()==true){
            throw new NotFound("Your Account already verify");
        }
        optsService.deleteOptCode(email);
        int randomNumber = (int) (Math.random() * 1000000);
        String formattedNumber = String.format("%06d", randomNumber);
        try {
            emailingService.sendMail(email,"Verify", formattedNumber);
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
        optsService.insertOpt(formattedNumber, false, userId.getId());

        return ResponseEntity.status(HttpStatus.OK).body("New Otp has been send");
    }

    @PutMapping("forget")
    public ResponseEntity<?> forgetPassword(@RequestParam @Email String email, @RequestBody @Valid AppUserRequestPassword appUserRequestPassword) {
        appUserService.loadUserByUsername(email);
        if(!appUserRequestPassword.getPassword().equals(appUserRequestPassword.getConfirmPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match");
        }
        String encryptedPassword = passwordEncoder.encode(appUserRequestPassword.getPassword());
        appUserRequestPassword.setPassword(encryptedPassword);
        appUserService.changePassword(encryptedPassword,email);
        return ResponseEntity.status(HttpStatus.OK).body("Password has been update Successfully");
    }
}
