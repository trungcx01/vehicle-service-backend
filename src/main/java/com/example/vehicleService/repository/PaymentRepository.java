package com.example.vehicleService.repository;

import com.example.vehicleService.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByTransactionReference(String transactionReference);
}
