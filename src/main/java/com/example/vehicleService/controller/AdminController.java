package com.example.vehicleService.controller;

import com.example.vehicleService.dto.ResponseMessage;
import com.example.vehicleService.entity.*;
import com.example.vehicleService.repository.NotificationRepository;
import com.example.vehicleService.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private VehicleCareService vehicleCareService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private EmergencyRequestService emergencyRequestService;

    @Autowired
    private ProposalService proposalService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PutMapping("/vehicle-care/{id}")
    public ResponseEntity<?> adminDeleteVehicleCare(@PathVariable Long id, @RequestParam String reason) {
        VehicleCare vehicleCare = vehicleCareService.getById(id);
        if (vehicleCare == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("VehicleCare not found!", LocalDateTime.now()));
        }
        Notification notification = new Notification();
        notification.setMessage("VehicleCare ID " + id + " has been deleted. Reason: " + reason);
        notification.setUsers(Set.of(vehicleCare.getShop().getUser()));

        notificationRepository.save(notification);
        simpMessagingTemplate.convertAndSendToUser(vehicleCare.getShop().getUser().getUsername(),
                "queue/notifications",
                "NOTIFICATION: VehicleCare ID " + id + " has been deleted. Reason: " + reason);
        vehicleCareService.delete(id);
        return ResponseEntity.ok(new ResponseMessage("Delete VehicleCare successfully!", LocalDateTime.now()));
    }

    @PutMapping("/review/{id}")
    public ResponseEntity<?> adminDeleteReview(@PathVariable Long id, @RequestParam String reason) {
        Review review = reviewService.getById(id);
        if (review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Review not found!", LocalDateTime.now()));
        }
        Notification notification = new Notification();
        notification.setMessage("Review ID " + id + " has been deleted. Reason: " + reason);
        User user;
        if (review.getServiceType().equals("APPOINTMENT")){
            Appointment appointment = appointmentService.getById(review.getBaseService().getId());
            user = appointment.getCustomer().getUser();
        } else{
            Proposal proposal = proposalService.getById(review.getBaseService().getId());
            user = proposal.getEmergencyRequest().getCustomer().getUser();
        }
        notification.setUsers(Set.of(user));
        notificationRepository.save(notification);

        simpMessagingTemplate.convertAndSendToUser(user.getUsername(),
                "queue/notifications",
                "NOTIFICATION: Review ID " + id + " has been deleted. Reason: " + reason);
        reviewService.delete(id);

        return ResponseEntity.ok(new ResponseMessage("Delete Review successfully!", LocalDateTime.now()));
    }
}
