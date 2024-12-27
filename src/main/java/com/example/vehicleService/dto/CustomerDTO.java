package com.example.vehicleService.dto;

import jakarta.validation.constraints.Null;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public class CustomerDTO {
    @Null
    private Long id;
    private String name;
    private String phoneNumber;
    private LocalDate dob;
    private String address;
    private String district;
    private Long userId;

    public CustomerDTO() {
    }

    public CustomerDTO(Long id, String name, String phoneNumber, LocalDate dob, String address, Long userId) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.address = address;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
