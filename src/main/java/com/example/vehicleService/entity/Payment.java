package com.example.vehicleService.entity;

import com.example.vehicleService.entity.enums.PaymentMethod;
import com.example.vehicleService.entity.enums.ServiceType;
import com.example.vehicleService.entity.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private Status status;

    @Column(name = "transaction_reference")
    private String transactionReference;

    @Column(name = "pay_link")
    @Size(max = 3000)
    private String payLink;

    @Column(name="order_info")
    @Size(max = 3000)
    private String orderInfo;

    @Column(name = "service_type")
    @Enumerated(value = EnumType.STRING)
    private ServiceType serviceType;

    @OneToOne
    @JoinColumn(name = "base_service_id", nullable = false)
    private BaseService baseService;

//    @AssertTrue(message = "Payment chỉ đc liên kết với Appointment hoặc EmergencyRequest")
//    public boolean isValidPayment(){
//        return (appointment != null && emergencyRequest == null)
//                || (appointment == null && emergencyRequest != null);
//    }

    //Sử dụng @Valid trong Service


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


    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getPayLink() {
        return payLink;
    }

    public void setPayLink(String payLink) {
        this.payLink = payLink;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public BaseService getBaseService() {
        return baseService;
    }



    public void setBaseService(BaseService baseService) {
        this.baseService = baseService;
    }
    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
}
