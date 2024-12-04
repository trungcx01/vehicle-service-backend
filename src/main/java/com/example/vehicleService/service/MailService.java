package com.example.vehicleService.service;

import com.example.vehicleService.dto.EmailDetail;

public interface MailService {
    void sendTextMail(EmailDetail mail);
}
