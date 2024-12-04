package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.EmailDetail;
import com.example.vehicleService.exception.BlogAPIException;
import com.example.vehicleService.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendTextMail(EmailDetail mail) {
       try {
           SimpleMailMessage mailMessage = new SimpleMailMessage();
           mailMessage.setFrom(sender);
           mailMessage.setTo(mail.getRecipient());
           mailMessage.setText(mail.getText());
           mailMessage.setSubject(mail.getSubject());
           javaMailSender.send(mailMessage);
       } catch (Exception ex){
           throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Error in sending mail!");
       }
    }
}
