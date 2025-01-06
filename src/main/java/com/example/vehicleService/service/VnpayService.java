package com.example.vehicleService.service;

import com.example.vehicleService.dto.PaymentDTO;
import com.example.vehicleService.dto.VnpayResponseDTO;
import com.example.vehicleService.entity.Payment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public interface VnpayService {
    VnpayResponseDTO createPayment(HttpServletRequest request, Long amount, String orderNote);
    PaymentDTO handleVnpayPaymentReturn(HttpServletRequest request,   HttpServletResponse response) throws IOException;
}
