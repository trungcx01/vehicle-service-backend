package com.example.vehicleService.repository;

import com.example.vehicleService.entity.Notification;
import com.example.vehicleService.entity.User;
import com.example.vehicleService.entity.enums.NotificationStatus;
import com.example.vehicleService.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUsers(Set<User> users);
//    List<Notification> findByUsersAndNotificationStatus(Set<User> users, NotificationStatus notificationStatus);
}
