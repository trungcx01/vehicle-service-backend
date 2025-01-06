package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.AppointmentDTO;
import com.example.vehicleService.entity.Appointment;
import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.mapper.AppointmentMapper;
import com.example.vehicleService.repository.AppointmentRepository;
import com.example.vehicleService.repository.CustomerRepository;
import com.example.vehicleService.repository.ShopRepository;
import com.example.vehicleService.repository.VehicleCareRepository;
import com.example.vehicleService.service.AppointmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final CustomerRepository customerRepository;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentRepository appointmentRepository;
    private final VehicleCareRepository vehicleCareRepository;
    private final ShopRepository shopRepository;

    public AppointmentServiceImpl(CustomerRepository customerRepository, AppointmentMapper appointmentMapper, AppointmentRepository appointmentRepository, VehicleCareRepository vehicleCareRepository, ShopRepository shopRepository) {
        this.customerRepository = customerRepository;
        this.appointmentMapper = appointmentMapper;
        this.appointmentRepository = appointmentRepository;
        this.vehicleCareRepository = vehicleCareRepository;
        this.shopRepository = shopRepository;
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
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO, customerRepository, vehicleCareRepository);
        LocalDateTime dateAndTime = appointmentDTO.getDate().atTime(appointmentDTO.getTime());
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
    public void updateStatus(Status status, Integer appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new EntityNotFoundException("Not found Appointment!")
        );
        appointment.setStatus(status);
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
