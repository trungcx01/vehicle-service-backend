package com.example.vehicleService.entity;

import com.example.vehicleService.entity.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "proposal")
@DiscriminatorValue("PROPOSAL")
@NoArgsConstructor
@AllArgsConstructor
public class Proposal extends BaseService{
    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne
    @JoinColumn(name = "emergency_request_id", nullable = false)
    private EmergencyRequest emergencyRequest;

    @Column(name = "predict", nullable = false)
    @Size(max = 10000)
    private String predict;

    @Column(name = "expected_price", nullable = false)
    private Long expectedPrice;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public EmergencyRequest getEmergencyRequest() {
        return emergencyRequest;
    }

    public void setEmergencyRequest(EmergencyRequest emergencyRequest) {
        this.emergencyRequest = emergencyRequest;
    }

    public String getPredict() {
        return predict;
    }

    public void setPredict(String predict) {
        this.predict = predict;
    }

    public Long getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(Long expectedPrice) {
        this.expectedPrice = expectedPrice;
    }

}
