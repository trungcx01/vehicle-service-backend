package com.example.vehicleService.service;

import com.example.vehicleService.dto.PaymentDTO;
import com.example.vehicleService.dto.ShopDTO;
import com.example.vehicleService.entity.Payment;
import com.example.vehicleService.entity.Shop;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    Payment getById(Long id);
    Page<Payment> getAllPagination(Pageable pageable);
    Payment save(PaymentDTO paymentDTO, HttpServletRequest request);
    void deleteById(Long id);
}
