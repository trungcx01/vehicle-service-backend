package com.example.vehicleService.dto;

import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.entity.enums.VehicleType;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class EmergencyRequestDTO {
    @Null
    private Integer id;
    private String description;
    private String imageDetail;
    private String location;
    private String licensePlate;
    private VehicleType vehicleType;
    private Status requestStatus = Status.PENDING;
    private Integer customerId;
    private Set<Integer> proposalIds;


  

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageDetail() {
        return imageDetail;
    }

    public void setImageDetail(String imageDetail) {
        this.imageDetail = imageDetail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Status getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Status requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Set<Integer> getProposalIds() {
        return proposalIds;
    }

    public void setProposalIds(Set<Integer> proposalIds) {
        this.proposalIds = proposalIds;
    }
}
