package com.example.vehicleService.service;

import com.example.vehicleService.dto.AppointmentDTO;
import com.example.vehicleService.entity.Appointment;
import com.example.vehicleService.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    Appointment getById(Integer id);
    Page<Appointment> getAllPagination(Pageable pageable);
    Appointment save(AppointmentDTO appointmentDTO);
    void delete(Integer id);
    List<Appointment> getByCurrentCustomer();
    Page<Appointment> getByCurrentShop(Pageable pageable);
    void updateStatus(Status status, Integer appointmentId);
    Integer countByDate(LocalDate date);
    Integer countByDateAndCurrentShop(LocalDate date);
    long count();
    long countByCurrentShop();

    Page<Appointment> searchAppointments(String searchTerm, Pageable pageable);
    Page<Appointment> searchAppointmentsInCurrentShop(String searchTerm, Pageable pageable);
}
