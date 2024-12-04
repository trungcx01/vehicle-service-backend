package com.example.vehicleService.service;

import com.example.vehicleService.dto.CustomerDTO;
import com.example.vehicleService.dto.EmergencyRequestDTO;
import com.example.vehicleService.entity.Customer;
import com.example.vehicleService.entity.EmergencyRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmergencyRequestService {
    EmergencyRequest getById(Long id);
    Page<EmergencyRequest> getAllPagination(Pageable pageable);
    EmergencyRequest save(MultipartFile image1, MultipartFile image2, MultipartFile image3, EmergencyRequestDTO emergencyRequestDTO);
    void deleteById(Long id);
}
