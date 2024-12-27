package com.example.vehicleService.entity;

import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.entity.enums.VehicleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "emergency_request")
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyRequest extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image_detail")
    @Size(max = 1000)
    private String imageDetail;

    @Column(name = "location", nullable = false)
    private String location;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "request_status", nullable = false)
    private Status requestStatus;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

//    @JoinColumn(name = "selected_proposal_id")
//    private Long selectedProposalId;

    @OneToMany(mappedBy = "emergencyRequest")
    @JsonIgnoreProperties("emergencyRequest")
    private Set<Proposal> proposals = new HashSet<>();

    @Override
    public String toString() {
       return id + "-" + customer.getName() + "-" + location + "-" +  vehicleType.toString() + "-";

    }

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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<Proposal> getProposals() {
        return proposals;
    }

    public void setProposals(Set<Proposal> proposals) {
        this.proposals = proposals;
    }

    public Status getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Status requestStatus) {
        this.requestStatus = requestStatus;
    }
}
