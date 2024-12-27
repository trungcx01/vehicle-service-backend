package com.example.vehicleService.repository;

import com.example.vehicleService.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r ORDER BY r.createAt DESC")
    List<Review> findTop8ByOrderByCreatedAtDesc();

    Review findByBaseServiceId(Long id);

    @Query(value = "SELECT r.* FROM review r \n" +
            "JOIN base_service bs ON r.base_service_id = bs.id \n" +
            "LEFT JOIN appointment a ON a.id = bs.id \n" +
            "LEFT JOIN appointment_vehicle_care avc ON avc.appointment_id = a.id \n" +
            "LEFT JOIN vehicle_care vc ON vc.id = avc.vehicle_care_id \n" +
            "LEFT JOIN proposal p ON p.id = bs.id \n" +
            "WHERE vc.shop_id = :shopId OR p.shop_id = :shopId", nativeQuery = true)
    List<Review> findByShopId(Long shopId);

}
