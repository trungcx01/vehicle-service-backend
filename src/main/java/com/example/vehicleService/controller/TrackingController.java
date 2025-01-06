package com.example.vehicleService.controller;

import com.example.vehicleService.dto.ResponseMessage;
import com.example.vehicleService.entity.Proposal;
import com.example.vehicleService.exception.BlogAPIException;
import com.example.vehicleService.repository.EmergencyRequestRepository;
import com.example.vehicleService.service.EmergencyRequestService;
import com.example.vehicleService.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class TrackingController {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmergencyRequestService emergencyRequestService;
    private final ProposalService proposalService;

    @Autowired
    public TrackingController(SimpMessagingTemplate messagingTemplate, RedisTemplate<String, String> redisTemplate, EmergencyRequestService emergencyRequestService, ProposalService proposalService) {
        this.messagingTemplate = messagingTemplate;
        this.redisTemplate = redisTemplate;
        this.emergencyRequestService = emergencyRequestService;
        this.proposalService = proposalService;
    }

    @MessageMapping("/send-location/{proposalId}")
    public void sendLocation(@DestinationVariable String proposalId, String message) {
        String redisKey = "shop:emergency-request:location:history:" + proposalId;
        System.out.println("udhudejiededdddddddddd" + message);
        String location = message.split(" ")[1];
        redisTemplate.opsForList().rightPush(redisKey, location);
        messagingTemplate.convertAndSend("/topic/proposal/" + proposalId, message);
    }

    @GetMapping("/api/get-shop-last-location/{proposalId}")
    public ResponseEntity<?> getLastLocation(@PathVariable Integer proposalId) {
        String redisKey = "shop:emergency-request:location:history:" + proposalId;
        List<String> locationHistory = redisTemplate.opsForList().range(redisKey, 0, -1);
        for (String x : locationHistory){
            System.out.println(x);
        }
        if (locationHistory != null && !locationHistory.isEmpty()) {
            return ResponseEntity.ok(new ResponseMessage(locationHistory.get(locationHistory.size() - 1), LocalDateTime.now()));
        }
        return ResponseEntity.ok(new BlogAPIException(HttpStatus.BAD_REQUEST, "Cửa hàng chưa di chuyển!"));
    }
}
