package com.example.vehicleService.controller;

import com.example.vehicleService.service.NotificationService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPagination(Pageable pageable){
        return ResponseEntity.ok(notificationService.getAllPagination(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return ResponseEntity.ok(notificationService.getById(id));
    }

    @GetMapping("current-user")
    public ResponseEntity<?> getByCurrentUser(){
        return ResponseEntity.ok(notificationService.getByCurrentUser());
    }

    @GetMapping("current-user/unread")
    public ResponseEntity<?> getUnreadOfCurrentUser(){
        return ResponseEntity.ok(notificationService.getUnreadNotificationOfCurrentUser());
    }

//    @PostMapping("/send-message")
//    public void sendLocationToCustomer()
}
