package com.example.vehicleService.service.impl;

import com.example.vehicleService.entity.Notification;
import com.example.vehicleService.entity.User;
import com.example.vehicleService.entity.enums.NotificationStatus;
import com.example.vehicleService.repository.NotificationRepository;
import com.example.vehicleService.repository.UserRepository;
import com.example.vehicleService.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final RedisTemplate redisTemplate;

    public NotificationServiceImpl(SimpMessagingTemplate simpMessagingTemplate, NotificationRepository notificationRepository, UserRepository userRepository, RedisTemplate redisTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    //    @Override
//    public void sendNotification(String destination, String message) {
//        Notification notification = new Notification();
//        notification.setMessage(message);
//        notification.setType("UNREAD");
//        notification.setUsers(userRepository.findAll().stream().collect(Collectors.toSet()));
//
//        simpMessagingTemplate.convertAndSend(destination, message);
//    }

//    @Override
//    public void send(String channel, String message) {
//        simpMessagingTemplate.convertAndSend(channel, message);
//    }
//
//    @Override
//    public void sendToUser(String username, String channel, String message) {
//        simpMessagingTemplate.convertAndSendToUser(username, channel, message);
//        System.out.println("dcm nha mau");
//    }

    @Override
    public Page<Notification> getAllPagination(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    @Override
    public Notification getById(Long id) {
        return notificationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Notification!")
        );
    }

//    @Override
//    public List<Notification> getUnreadNotificationOfCurrentUser() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.findByUsername(username).orElseThrow(
//                () -> new EntityNotFoundException("Not found user!")
//        );
//        return notificationRepository.findByUsersAndNotificationStatus(Set.of(user), NotificationStatus.UNREAD);
//    }

    @Override
    public List<Notification> getByCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("Not found user!")
        );
        return notificationRepository.findByUsers(Set.of(user));
    }

    @Override
    public void readAllNotificationsOfCurrentUser() {
//        List<Notification> notificationList = getByCurrentUser();
//        for (Notification n : notificationList){
//            n.setNotificationStatus(NotificationStatus.READ);
//        }
//        notificationRepository.saveAll(notificationList);
    }

    @Override
    public void readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new EntityNotFoundException("Not found Notification!")
        );
//        notification.setNotificationStatus(NotificationStatus.READ);
        notificationRepository.save(notification);
    }

    @Override
    public void publishLiveTracking(String message, Long proposalId) {
        redisTemplate.convertAndSend("live-tracking/" + proposalId, message);
    }
}
