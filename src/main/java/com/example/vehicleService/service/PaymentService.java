package com.example.vehicleService.service;

import com.example.vehicleService.dto.PaymentDTO;
import com.example.vehicleService.entity.Payment;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PaymentService {
    Payment getById(Integer id);
    Page<Payment> getAllPagination(Pageable pageable);
    Payment save(PaymentDTO paymentDTO, HttpServletRequest request);
    void deleteById(Integer id);
//    Payment getPaymentOfRequest(Integer id);
    Payment getPaymentOfAppointment(Integer id);
    Integer totalAmountByDateAndCurrentShop(LocalDate date);
    Long getTotalRevenue();
//    List<Payment> getFinishedByShop();
}
