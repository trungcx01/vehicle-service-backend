package com.example.vehicleService.dto;

import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.entity.enums.VehicleType;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    @Null
    private Integer id;
    private LocalDate date;
    private LocalTime time;
    private VehicleType vehicleType;
    private String note;
    private Set<Integer> vehicleCareIds = new HashSet<>();
    private Integer customerId;
    private Status status = Status.PENDING;

    public Status getStatus() {
        return status;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
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

    public Set<Integer> getVehicleCareIds() {
        return vehicleCareIds;
    }

    public void setVehicleCareIds(Set<Integer> vehicleCareIds) {
        this.vehicleCareIds = vehicleCareIds;
    }
}
