package com.example.minispring.service.Implementation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.minispring.model.Request.MailRequest;
import com.example.minispring.service.EmailingService;

@Service
@RequiredArgsConstructor
public class EmailingServiceImp implements EmailingService{
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Async
    public void sendMail(String email,String subject,String formattedNumber) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom(fromMail);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);

        Context context = new Context();
        context.setVariable("optscode", formattedNumber);
        String processedString = templateEngine.process("template", context);
            
        mimeMessageHelper.setText(processedString, true);

        mailSender.send(mimeMessage);
    }
}
