package com.example.vehicleService.controller;

import com.example.vehicleService.dto.CustomerDTO;
import com.example.vehicleService.dto.ResponseMessage;
import com.example.vehicleService.dto.ShopDTO;
import com.example.vehicleService.entity.Customer;
import com.example.vehicleService.service.CustomerService;
import com.example.vehicleService.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/customers")
public class CustomerController {
    private final CustomerService customerService;
    private final UserService userService;

    public CustomerController(CustomerService customerService, UserService userService) {
        this.customerService = customerService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = customerService.save(customerDTO);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<?> getAllCustomers(Pageable pageable){
        return ResponseEntity.ok(customerService.getAllPagination(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id){
        return ResponseEntity.ok(customerService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer id){
        customerService.delete(id);
        return ResponseEntity.ok(new ResponseMessage("Delete customer with id " + id + " successfully!", LocalDateTime.now()));
    }

    @PutMapping
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = customerService.save(customerDTO);
        return ResponseEntity.ok(customer);
    }
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentCustomer(){
        return ResponseEntity.ok(customerService.getCurrentCustomer());
    }

    @PutMapping("/update-info")
    public ResponseEntity<?> updateInfo(@RequestParam("avatar") MultipartFile avatar, @RequestBody CustomerDTO customerDTO){
        userService.updateAvatar(avatar, null);
        customerService.save(customerDTO);
        return ResponseEntity.ok(new ResponseMessage("Cập nhật thông tin thành công", LocalDateTime.now()));
    }

    @GetMapping("/get-by-phone")
    public ResponseEntity<?> getByPhone(@RequestParam("phoneNumber") String phoneNumber){
        return ResponseEntity.ok(customerService.getByPhoneNumber(phoneNumber));
    }

    @GetMapping("/search")
    public Page<Customer> searchCustomers(
            @RequestParam String searchTerm,
            @RequestParam int page,
            @RequestParam int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);
        return customerService.searchCustomers(searchTerm, pageable);
    }
}
