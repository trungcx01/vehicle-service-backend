package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.CustomerDTO;
import com.example.vehicleService.entity.Customer;
import com.example.vehicleService.entity.Role;
import com.example.vehicleService.entity.User;
import com.example.vehicleService.mapper.CustomerMapper;
import com.example.vehicleService.repository.CustomerRepository;
import com.example.vehicleService.repository.RoleRepository;
import com.example.vehicleService.repository.UserRepository;
import com.example.vehicleService.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;

    public CustomerServiceImpl(UserRepository userRepository, CustomerMapper customerMapper, CustomerRepository customerRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Customer save(CustomerDTO customerDTO) {
        Customer customer = customerMapper.toEntity(customerDTO, userRepository);
        if (customerDTO.getId() == null){
            Role role = roleRepository.findByName("CUSTOMER").orElseThrow(
                    () -> new EntityNotFoundException("Not found Role CUSTOMER")
            );
            customer.getUser().setRoles(Set.of(role));
        }
        return customerRepository.save(customer);
    }

    @Override
    public Customer getById(Integer id) {
        return customerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found customer!")
        );
    }

    @Override
    public Page<Customer> searchCustomers(String searchTerm, Pageable pageable) {
        return customerRepository.searchCustomers(searchTerm, pageable);
    }

    @Override
    public Page<Customer> getAllPagination(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public void delete(Integer id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Not found Customer!")
        );
        customer.setDeleted(true);
        customer.getUser().setLocked(true);
        customerRepository.save(customer);
    }

    @Override
    public Customer getCurrentCustomer() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return customerRepository.findByUserUsername(username).orElseThrow(
                () -> new EntityNotFoundException("Not found Customer!")
        );
    }

    @Override
    public Customer getByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new EntityNotFoundException("Not found Customer!")
        );
    }
}
