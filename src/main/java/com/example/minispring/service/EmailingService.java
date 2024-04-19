package com.example.minispring.service;

import jakarta.mail.MessagingException;

public interface EmailingService {
    void sendMail(String email,String subject,String formattedNumber) throws MessagingException;
}
