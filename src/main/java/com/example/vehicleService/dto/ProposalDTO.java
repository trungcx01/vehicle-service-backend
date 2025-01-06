package com.example.vehicleService.dto;

import com.example.vehicleService.entity.enums.Status;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ProposalDTO {
    @Null
    private Integer id;
    private Integer shopId;
    private Integer emergencyRequestId;
    private Long expectedPrice;
    private String predict;
    private Status status = Status.PENDING;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getEmergencyRequestId() {
        return emergencyRequestId;
    }

    public void setEmergencyRequestId(Integer emergencyRequestId) {
        this.emergencyRequestId = emergencyRequestId;
    }

    public Long getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(Long expectedPrice) {
        this.expectedPrice = expectedPrice;
    }

    public String getPredict() {
        return predict;
    }

    public void setPredict(String predict) {
        this.predict = predict;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
