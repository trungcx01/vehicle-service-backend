package com.example.vehicleService.repository;

import com.example.vehicleService.entity.Customer;
import com.example.vehicleService.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUserUsername(String username);
    Optional<Customer> findByPhoneNumber(String phoneNumber);
}
