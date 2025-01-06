package com.example.vehicleService.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {

    @Value("${sms.api.key}")
    private String apiKey;

    @Value("${sms.sender}")
    private String sender;

    private static final String API_URL = "https://api.speedsms.vn/index.php/sms/send";

    public void sendSms(String phoneNumber, String messageContent) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(apiKey, "");


        Map<String, Object> body = new HashMap<>();
        body.put("to", new String[]{phoneNumber});
        body.put("content", messageContent);
        body.put("sms_type", 2);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("SMS sent successfully to " + phoneNumber);
            } else {
                System.out.println("Failed to send SMS to " + phoneNumber + ". Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error occurred while sending SMS to " + phoneNumber + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendBulkSms(String[] phoneNumbers, String messageContent) {
        for (String phoneNumber : phoneNumbers) {
            sendSms(phoneNumber, messageContent);
        }
    }
}
