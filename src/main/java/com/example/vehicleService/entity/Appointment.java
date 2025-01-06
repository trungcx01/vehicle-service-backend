package com.example.vehicleService.entity;

import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.entity.enums.VehicleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "appointment")
@DiscriminatorValue("APPOINTMENT")
@NoArgsConstructor
@AllArgsConstructor
public class Appointment extends BaseService{
    @Column(name = "date_and_time", nullable = false)
    private LocalDateTime dateAndTime;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

//    @Enumerated(value = EnumType.STRING)
//    @Column(name = "status", nullable = false)
//    private Status status;

    @Column(name = "note")
    private String note;

    @ManyToMany
    @JoinTable(
            name = "appointment_vehicle_care",
            joinColumns = {@JoinColumn(name = "appointment_id", referencedColumnName = "id")},  // thực thể chính
            inverseJoinColumns = {@JoinColumn(name = "vehicle_care_id", referencedColumnName = "id")} // thực thể đối tác
    )
    private Set<VehicleCare> vehicleCares = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<VehicleCare> getVehicleCares() {
        return vehicleCares;
    }

    public void setVehicleCares(Set<VehicleCare> vehicleCares) {
        this.vehicleCares = vehicleCares;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
