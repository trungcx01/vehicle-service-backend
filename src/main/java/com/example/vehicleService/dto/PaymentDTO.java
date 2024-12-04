package com.example.vehicleService.dto;

import com.example.vehicleService.entity.Appointment;
import com.example.vehicleService.entity.EmergencyRequest;
import com.example.vehicleService.entity.enums.PaymentMethod;
import com.example.vehicleService.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    @Null
    private Long id;
    private double amount;
    private PaymentMethod paymentMethod;
    private Status paymentStatus;
    @Null
    private Long appointmentId;
    @Null
    private Long emergencyRequestId;
    private String transactionReference;

//    @Null
//    private String returnUrl;

    @AssertTrue(message = "Payment chỉ đc liên kết với Appointment hoặc EmergencyRequest")
    public boolean isValidPayment(){
        return (appointmentId != null && emergencyRequestId == null)
                || (appointmentId == null && emergencyRequestId != null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Status getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Status paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getEmergencyRequestId() {
        return emergencyRequestId;
    }

    public void setEmergencyRequestId(Long emergencyRequestId) {
        this.emergencyRequestId = emergencyRequestId;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

//    public String getReturnUrl() {
//        return returnUrl;
//    }
//
//    public void setReturnUrl(String returnUrl) {
//        this.returnUrl = returnUrl;
//    }
}
