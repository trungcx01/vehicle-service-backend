package com.example.vehicleService.service;

import com.example.vehicleService.dto.PaymentDTO;
import com.example.vehicleService.entity.Payment;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PaymentService {
    Payment getById(Long id);
    Page<Payment> getAllPagination(Pageable pageable);
    Payment save(PaymentDTO paymentDTO, HttpServletRequest request);
    void deleteById(Long id);
//    Payment getPaymentOfRequest(Long id);
    Payment getPaymentOfAppointment(Long id);
    Long totalAmountByDateAndCurrentShop(LocalDate date);

//    List<Payment> getFinishedByShop();
}
