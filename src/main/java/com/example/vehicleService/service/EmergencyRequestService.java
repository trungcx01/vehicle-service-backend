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
    EmergencyRequest getById(Integer id);
    Page<EmergencyRequest> getAllPagination(Pageable pageable);
    EmergencyRequest save(MultipartFile image1, MultipartFile image2, MultipartFile image3, EmergencyRequestDTO emergencyRequestDTO);
    void deleteById(Integer id);
    void updateStatus(Status status, Integer id);
    Integer countByDate(LocalDate date);
    Integer countByDateAndCurrentShop(LocalDate date);
    List<EmergencyRequest> getByCustomer();
    long count();
    long countByCurrentShop();
    Page<EmergencyRequest> searchEmergencyRequests(String searchTerm, Pageable pageable);
}
