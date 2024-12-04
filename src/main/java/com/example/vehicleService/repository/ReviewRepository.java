package com.example.vehicleService.repository;

import com.example.vehicleService.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r ORDER BY r.createdAt DESC")
    List<Review> findTop8ByOrderByCreatedAtDesc();
}
