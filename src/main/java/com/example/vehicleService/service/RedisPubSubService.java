package com.example.vehicleService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisPubSubService implements MessageListener {
    private final RedisTemplate redisTemplate;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public RedisPubSubService(RedisTemplate redisTemplate, NotificationService notificationService, SimpMessagingTemplate simpMessagingTemplate) {
        this.redisTemplate = redisTemplate;
        this.notificationService = notificationService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        String userId = new String(pattern).substring(5);
        if (userId != null){
            simpMessagingTemplate.convertAndSend("/topic/" + channel, body);
        }else{
            simpMessagingTemplate.convertAndSendToUser(userId, "/queue/proposal", body);
        }
    }

    public void publishMessage(String channel, String message){
        redisTemplate.convertAndSend(channel, message);
    }
    public void publishMessagePrivate(String userId, String message){
        redisTemplate.convertAndSend("user-" + userId, message);
    }
}
