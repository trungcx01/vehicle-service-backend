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
    private Integer id;
    private long amount;
    private PaymentMethod paymentMethod;
    private Status status;
    @Null
    private Integer appointmentId;
    @Null
    private Integer proposalId;
    private String transactionReference;

//    @Null
//    private String returnUrl;

//    @AssertTrue(message = "Payment chỉ đc liên kết với Appointment hoặc EmergencyRequest")
//    public boolean isValidPayment(){
//        return (appointmentId != null && proposalId == null)
//                || (appointmentId == null && proposalId != null);
//    }


    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Integer getProposalId() {
        return proposalId;
    }

    public void setProposalId(Integer proposalId) {
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
