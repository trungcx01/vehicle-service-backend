package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.AppointmentDTO;
import com.example.vehicleService.entity.Appointment;
import com.example.vehicleService.entity.Notification;
import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.VehicleCare;
import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.exception.BlogAPIException;
import com.example.vehicleService.mapper.AppointmentMapper;
import com.example.vehicleService.repository.*;
import com.example.vehicleService.service.AppointmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final CustomerRepository customerRepository;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final VehicleCareRepository vehicleCareRepository;
    private final ShopRepository shopRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationRepository notificationRepository;

    public AppointmentServiceImpl(CustomerRepository customerRepository, AppointmentMapper appointmentMapper, AppointmentRepository appointmentRepository, VehicleCareRepository vehicleCareRepository, ShopRepository shopRepository, SimpMessagingTemplate simpMessagingTemplate, NotificationRepository notificationRepository) {
        this.customerRepository = customerRepository;
        this.appointmentMapper = appointmentMapper;
        this.appointmentRepository = appointmentRepository;
        this.vehicleCareRepository = vehicleCareRepository;
        this.shopRepository = shopRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Appointment getById(Integer id) {
        return appointmentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Appointment!")
        );
    }

    @Override
    public Page<Appointment> getAllPagination(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }

    @Override
    public Appointment save(AppointmentDTO appointmentDTO) {
        LocalDateTime dateAndTime = appointmentDTO.getDate().atTime(appointmentDTO.getTime());
        LocalDateTime now = LocalDateTime.now();
        if (dateAndTime.isBefore(now)) {
            throw new BlogAPIException(
                    HttpStatus.BAD_REQUEST,   String.format("Ngày và giờ hẹn phải lớn hơn thời điểm hiện tại."));
        }

        VehicleCare vehicleCare = vehicleCareRepository.findById(appointmentDTO.getVehicleCareIds().stream().toList().get(0)).orElseThrow(
                () -> new EntityNotFoundException("Not found Vehicle Care!")
        );
        Shop shop = vehicleCare.getShop();
        LocalTime openHour = shop.getOpenHour();
        LocalTime closeHour = shop.getCloseHour();

        if (appointmentDTO.getTime().isBefore(openHour) || appointmentDTO.getTime().isAfter(closeHour)) {
            throw new BlogAPIException(
                  HttpStatus.BAD_REQUEST,   String.format("Thời gian hẹn phải nằm trong khoảng %s - %s.", openHour, closeHour));
        }
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO, customerRepository, vehicleCareRepository);
        appointment.setDateAndTime(dateAndTime);
        return appointmentRepository.save(appointment);
    }

    @Override
    public void delete(Integer id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Appointment!")
        );
        appointmentRepository.delete(appointment);
    }

    @Override
    public List<Appointment> getByCurrentCustomer() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return appointmentRepository.findByCustomerUserUsername(username);
    }

    @Override
    public Page<Appointment> getByCurrentShop(Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return appointmentRepository.findByVehicleCaresShopUserUsername(username, pageable);
    }

    @Override
    @Transactional
    public void updateStatus(Status status, Integer appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new EntityNotFoundException("Not found Appointment!")
        );
        appointment.setStatus(status);
        String message = null;
        if (status.equals(Status.ACCEPTED)) {
            message = "Lịch hẹn của bạn đã được chấp nhận";
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setUsers(Set.of(appointment.getCustomer().getUser()));
            notificationRepository.save(notification);
        }else if (status.equals(Status.CANCELED)){
            message = "Lịch hẹn của bạn đã bị từ chối";
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setUsers(Set.of(appointment.getCustomer().getUser()));
            notificationRepository.save(notification);
        }
        simpMessagingTemplate.convertAndSendToUser(appointment.getCustomer().getUser().getUsername(), "queue/notifications", "NOTIFICATION: " + message);
        appointmentRepository.save(appointment);
    }

    @Override
    public Integer countByDate(LocalDate date) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Shop shop = shopRepository.findByUserUsername(username);
//        Integer shopId = shop.getId();
        return appointmentRepository.countByDate(date);
    }


    @Override
    public Integer countByDateAndCurrentShop(LocalDate date) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Shop shop = shopRepository.findByUserUsername(username);
        return  appointmentRepository.countByCurrentShopAndDate(date, shop.getId());
    }

    @Override
    public long count() {
        return appointmentRepository.count();
    }

    @Override
    public long countByCurrentShop() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Shop shop = shopRepository.findByUserUsername(username);
        return appointmentRepository.countByCurrentShop(shop.getId());
    }

    @Override
    public Page<Appointment> searchAppointments(String searchTerm, Pageable pageable) {
        return appointmentRepository.searchAppointments(searchTerm, pageable);
    }

    @Override
    public Page<Appointment> searchAppointmentsInCurrentShop(String searchTerm, Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Shop shop = shopRepository.findByUserUsername(username);
        return appointmentRepository.searchAppointmentsInShop(searchTerm, shop.getId(), pageable);
    }
}
