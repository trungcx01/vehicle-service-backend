package com.example.vehicleService.service;

import com.example.vehicleService.dto.CustomerDTO;
import com.example.vehicleService.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CustomerService {
    Customer getById(Integer id);
    Page<Customer> getAllPagination(Pageable pageable);
    Customer save(CustomerDTO customerDTO);
    void delete(Integer id);
    Customer getCurrentCustomer();
    Customer getByPhoneNumber(String phoneNumber);
    Page<Customer> searchCustomers(String searchTerm, Pageable pageable);
}
