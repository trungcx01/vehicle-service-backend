package com.example.vehicleService.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TrackingController {

    private final SimpMessagingTemplate messagingTemplate;

    public TrackingController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/send-location/{proposalId}")
    public void sendLocation(String location, @DestinationVariable String proposalId) {
        // Gửi thông tin vị trí đến topic chung cho proposalId
        messagingTemplate.convertAndSend("/topic/proposal/" + proposalId, location);
    }
}
