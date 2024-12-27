package com.example.vehicleService.service;

import com.example.vehicleService.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
//    void sendNotification(String destination, String message);
//    void send(String channel, String message);
//    void sendToUser(String userId, String channel, String message);
    Page<Notification> getAllPagination(Pageable pageable);
    Notification getById(Long id);
//    List<Notification> getUnreadNotificationOfCurrentUser();
    List<Notification> getByCurrentUser();
    void readAllNotificationsOfCurrentUser();
    void readNotification(Long notificationId);
    void publishLiveTracking(String message, Long proposalId);
}
