package com.example.vehicleService.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class LiveTrackingSubcriber implements MessageListener {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public LiveTrackingSubcriber(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());
        simpMessagingTemplate.convertAndSend("/topic/live-tracking", messageBody);
    }
}
