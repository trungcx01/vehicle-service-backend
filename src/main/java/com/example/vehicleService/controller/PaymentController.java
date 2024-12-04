package com.example.vehicleService.controller;

import com.example.vehicleService.dto.PaymentDTO;
import com.example.vehicleService.dto.ResponseMessage;
import com.example.vehicleService.service.PaymentService;
import com.example.vehicleService.service.VnpayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    private final VnpayService vnpayService;

    public PaymentController(PaymentService paymentService, VnpayService vnpayService) {
        this.paymentService = paymentService;
        this.vnpayService = vnpayService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable){
        return ResponseEntity.ok(paymentService.getAllPagination(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody PaymentDTO paymentDTO, HttpServletRequest request){
        return ResponseEntity.ok(paymentService.save(paymentDTO, request));
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody PaymentDTO paymentDTO, HttpServletRequest request){
        return ResponseEntity.ok(paymentService.save(paymentDTO, request));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        paymentService.deleteById(id);
        return ResponseEntity.ok(new ResponseMessage("Delete Payment successfully!", LocalDateTime.now()));
    }

    @GetMapping("vnpay/return")
    public ResponseEntity<?> handleReturnVnpay(HttpServletRequest request,   HttpServletResponse response) throws IOException {
        return ResponseEntity.ok(vnpayService.handleVnpayPaymentReturn(request, response));
    }
}
