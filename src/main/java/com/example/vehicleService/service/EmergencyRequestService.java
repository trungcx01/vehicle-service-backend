package com.example.vehicleService.service;

import com.example.vehicleService.dto.EmergencyRequestDTO;
import com.example.vehicleService.entity.EmergencyRequest;
import com.example.vehicleService.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface EmergencyRequestService {
    EmergencyRequest getById(Long id);
    Page<EmergencyRequest> getAllPagination(Pageable pageable);
    EmergencyRequest save(MultipartFile image1, MultipartFile image2, MultipartFile image3, EmergencyRequestDTO emergencyRequestDTO);
    void deleteById(Long id);
    void updateStatus(Status status, Long id);
    long countByDate(LocalDate date);
    long countByDateAndCurrentShop(LocalDate date);
    List<EmergencyRequest> getByCustomer();
}
