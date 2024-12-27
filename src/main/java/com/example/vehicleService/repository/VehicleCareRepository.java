package com.example.vehicleService.repository;

import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.VehicleCare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleCareRepository extends JpaRepository<VehicleCare, Long> {
    List<VehicleCare> findByShop(Shop shop);

    @Query(value = "SELECT vc.* FROM vehicle_care vc " +
            "JOIN shop s ON vc.shop_id = s.id " +
            "WHERE LOWER(vc.name) LIKE LOWER(CONCAT('%', ?1, '%')) " +
            "AND LOWER(s.district) LIKE LOWER(CONCAT('%', ?2, '%')) " +
            "AND vc.price >= ?3 AND vc.price <= ?4", nativeQuery = true)
    List<VehicleCare> searchVehicleCare(String name, String district, Long startPrice, Long endPrice);
}
