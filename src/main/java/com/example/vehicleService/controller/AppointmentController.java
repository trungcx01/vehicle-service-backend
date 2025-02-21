package com.example.vehicleService.controller;

import com.example.vehicleService.dto.AppointmentDTO;
import com.example.vehicleService.dto.EmailDetail;
import com.example.vehicleService.dto.ResponseMessage;
import com.example.vehicleService.entity.*;
import com.example.vehicleService.entity.enums.NotificationStatus;
import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.entity.enums.Event;
import com.example.vehicleService.repository.NotificationRepository;
import com.example.vehicleService.repository.UserRepository;
import com.example.vehicleService.service.AppointmentService;
import com.example.vehicleService.service.MailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepository userRepository;
    private final MailService mailService;

    public AppointmentController(AppointmentService appointmentService, NotificationRepository notificationRepository, SimpMessagingTemplate simpMessagingTemplate, UserRepository userRepository, MailService mailService) {
        this.appointmentService = appointmentService;
        this.notificationRepository = notificationRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        return ResponseEntity.ok(appointmentService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable){
        return ResponseEntity.ok(appointmentService.getAllPagination(pageable));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> add(@RequestBody AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentService.save(appointmentDTO);
        Notification notification = new Notification();

        Customer customer = appointment.getCustomer();
        Shop shop = appointment.getVehicleCares().stream().findFirst().orElseThrow().getShop();

        String message = customer.getName() + " đã đặt lịch hẹn sửa chữa " + appointment.getVehicleType() + " ở " + shop.getName();
//        notification.setNotificationStatus(NotificationStatus.UNREAD);
//        notification.setEventType(Event.APPOINTMENT);
//        notification.setEventId(appointment.getId());
        notification.setMessage(message);
//        mailService.sendTextMail(new EmailDetail(shop.getUser().getEmail(),
//                "Thông báo có lịch hẹn mới", message));

        // Lấy đối tượng User từ DB để tránh trạng thái Detached
        User shopUser = userRepository.findById(shop.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Shop user not found"));
        User customerUser = userRepository.findById(customer.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Customer user not found"));

        notification.setUsers(Set.of(shopUser, customerUser));

        notificationRepository.save(notification);
        notificationRepository.flush();

        simpMessagingTemplate.convertAndSendToUser(customerUser.getUsername(), "queue/notifications", "NOTIFICATION: " + message);
        simpMessagingTemplate.convertAndSendToUser(shopUser.getUsername(), "queue/notifications", "NOTIFICATION: " + message);

        return ResponseEntity.ok(appointment);
    }


    @PutMapping
    public ResponseEntity<?> update(@RequestBody AppointmentDTO appointmentDTO){
        return ResponseEntity.ok(appointmentService.save(appointmentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        appointmentService.delete(id);
        return ResponseEntity.ok(new ResponseMessage("Delete Appointment successfully!", LocalDateTime.now()));
    }

    @GetMapping("/customer")
    public ResponseEntity<?> getByCurrentCustomer(){
       return ResponseEntity.ok( appointmentService.getByCurrentCustomer());
    }

    @GetMapping("/shop")
    public ResponseEntity<?> getByCurrentShop(Pageable pageable){
        return ResponseEntity.ok( appointmentService.getByCurrentShop(pageable));
    }

    @PutMapping("update-status/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable int id, @RequestParam("status") Status status){
        appointmentService.updateStatus(status, id);
        return ResponseEntity.ok(new ResponseMessage("Cập nhật trạng thái thành công!", LocalDateTime.now()));
    }

    @GetMapping("count/{date}")
    public ResponseEntity<?> countByDate(@PathVariable String date){
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(appointmentService.countByDate(localDate));
    }

    @GetMapping("count-by-shop/{date}")
    public ResponseEntity<?> countByCurrentShopAndDate(@PathVariable String date){
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(appointmentService.countByDateAndCurrentShop(localDate));
    }

    @GetMapping("count")
    public ResponseEntity<?> count(){
        return ResponseEntity.ok(appointmentService.count());
    }

    @GetMapping("count-by-shop")
    public ResponseEntity<?> countByCurrentShop(){
        return ResponseEntity.ok(appointmentService.countByCurrentShop());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Appointment>> searchAppointments(
            @RequestParam(value = "searchTerm") String searchTerm,
            Pageable pageable) {

        Page<Appointment> appointmentsPage = appointmentService.searchAppointments(searchTerm, pageable);
        return ResponseEntity.ok(appointmentsPage);
    }

    @GetMapping("/search-in-shop")
    public ResponseEntity<Page<Appointment>> searchAppointmentsInShop(
            @RequestParam(value = "searchTerm") String searchTerm,
            Pageable pageable) {

        Page<Appointment> appointmentsPage = appointmentService.searchAppointmentsInCurrentShop(searchTerm, pageable);
        return ResponseEntity.ok(appointmentsPage);
    }
}
