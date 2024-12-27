package com.example.vehicleService.dto;

import com.example.vehicleService.entity.enums.PaymentMethod;
import com.example.vehicleService.entity.enums.Status;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    @Null
    private Long id;
    private double amount;
    private PaymentMethod paymentMethod;
    private Status status;
    @Null
    private Long appointmentId;
    @Null
    private Long proposalId;
    private String transactionReference;

//    @Null
//    private String returnUrl;

    @AssertTrue(message = "Payment chỉ đc liên kết với Appointment hoặc EmergencyRequest")
    public boolean isValidPayment(){
        return (appointmentId != null && proposalId == null)
                || (appointmentId == null && proposalId != null);
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getProposalId() {
        return proposalId;
    }

    public void setProposalId(Long proposalId) {
        this.proposalId = proposalId;
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
