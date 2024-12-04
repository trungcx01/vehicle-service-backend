package com.example.vehicleService.dto;

import com.example.vehicleService.entity.Customer;
import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.entity.enums.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class EmergencyRequestDTO {
    @Null
    private Long id;
    private String description;
    private String imageDetail;
    private String location;
    private String licensePlate;
    private VehicleType vehicleType;
    private Status requestStatus = Status.PENDING;
    private Long customerId;
    private Set<Long> proposalIds;
    @Null
    private Long selectedProposalId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Set<Long> getProposalIds() {
        return proposalIds;
    }

    public void setProposalIds(Set<Long> proposalIds) {
        this.proposalIds = proposalIds;
    }

    public Long getSelectedProposalId() {
        return selectedProposalId;
    }

    public void setSelectedProposalId(Long selectedProposalId) {
        this.selectedProposalId = selectedProposalId;
    }
}
