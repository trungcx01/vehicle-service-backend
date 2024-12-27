package com.example.vehicleService.service;

import com.example.vehicleService.dto.AppointmentDTO;
import com.example.vehicleService.entity.Appointment;
import com.example.vehicleService.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    Appointment getById(Long id);
    Page<Appointment> getAllPagination(Pageable pageable);
    Appointment save(AppointmentDTO appointmentDTO);
    void delete(Long id);
    List<Appointment> getByCurrentCustomer();
    List<Appointment> getByCurrentShop();
    void updateStatus(Status status, Long appointmentId);
    long countByDate(LocalDate date);
    long countByDateAndCurrentShop(LocalDate date);
}
