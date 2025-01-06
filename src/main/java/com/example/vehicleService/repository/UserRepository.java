package com.example.vehicleService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.vehicleService.entity.User;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    boolean existsByUsernameOrEmail(String username, String email);
    Optional<User> findByActivationKey(String key);
    Optional<User> findByResetKey(String key);

    @Query(value = "SELECT COUNT(*) FROM user u " +
            "WHERE DATE(u.created_at) = ?1", nativeQuery = true)
    Integer getUserByDate(LocalDate date);

    long count();
}
