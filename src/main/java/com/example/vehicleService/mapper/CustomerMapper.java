package com.example.vehicleService.mapper;

import com.example.vehicleService.dto.CustomerDTO;
import com.example.vehicleService.entity.Customer;
import com.example.vehicleService.entity.User;
import com.example.vehicleService.exception.BlogAPIException;
import com.example.vehicleService.repository.CustomerRepository;
import com.example.vehicleService.repository.UserRepository;
import com.example.vehicleService.service.AuthService;
import com.example.vehicleService.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer>{
    @Mapping(source = "userId", target = "user")
    Customer toEntity(CustomerDTO dto,  @Context UserRepository userRepository);

    @Mapping(source = "user.id", target = "userId")
    CustomerDTO toDto(Customer entity);

    default Customer fromId(Long id, @Context CustomerRepository customerRepository){
        if (id == null){
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           String username = authentication.getName();
           return customerRepository.findByUserUsername(username).orElseThrow(
                   () -> new EntityNotFoundException("Not found Customer!")
           );
        }
        return customerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Customer!")
        );
    }

}
