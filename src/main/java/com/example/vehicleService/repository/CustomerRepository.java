package com.example.vehicleService.repository;

import com.example.vehicleService.entity.Customer;
import com.example.vehicleService.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByUserUsername(String username);
    Optional<Customer> findByPhoneNumber(String phoneNumber);

    @Query(
            value = """
            SELECT c.* 
            FROM customer c
            LEFT JOIN user u ON c.user_id = u.id
            WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(c.phone_number) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(c.address) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(u.locked) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            """,
            countQuery = """
            SELECT COUNT(*) 
            FROM customer c
            LEFT JOIN user u ON c.user_id = u.id
            WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(c.phone_number) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(c.address) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(u.locked) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            """,
            nativeQuery = true
    )
    Page<Customer> searchCustomers(@Param("searchTerm") String searchTerm, Pageable pageable);
}
