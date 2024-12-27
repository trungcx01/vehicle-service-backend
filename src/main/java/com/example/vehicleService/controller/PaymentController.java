package com.example.vehicleService.controller;

import com.example.vehicleService.dto.PaymentDTO;
import com.example.vehicleService.dto.ResponseMessage;
import com.example.vehicleService.entity.Payment;
import com.example.vehicleService.service.PaymentService;
import com.example.vehicleService.service.VnpayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/appointment/{id}")
    public ResponseEntity<?> getPaymentOfAppointment(@PathVariable("id") Long id){
        return ResponseEntity.ok(paymentService.getPaymentOfAppointment(id));
    }

    @GetMapping("/total-by-shop/{date}")
    public ResponseEntity<?> totalAmountByDateAndCurrentShop(@PathVariable("date") String date){
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(paymentService.totalAmountByDateAndCurrentShop(localDate));
    }

//
//    @GetMapping("/emergency-request/{id}")
//    public ResponseEntity<?> getPaymentOfEmergencyRequest(@PathVariable("id") Long id){
//        return ResponseEntity.ok(paymentService.getPaymentOfRequest(id));
//    }
//
//    @GetMapping("/get-by-date/{date}")
//    public ResponseEntity<?> getTurnOverByDate( @PathVariable String date){
//        List<Payment> payments = paymentService.getFinishedByShop();
//        List<Payment> filteredPayments = payments.stream()
//                .filter(payment -> payment.getCreateAt().toLocalDate().equals(LocalDate.parse(date)))
//                .collect(Collectors.toList());
//
//
//        double totalTurnOver = filteredPayments.stream()
//                .mapToDouble(Payment::getAmount)
//                .sum();
//        return ResponseEntity.ok(totalTurnOver);
//    }
}
