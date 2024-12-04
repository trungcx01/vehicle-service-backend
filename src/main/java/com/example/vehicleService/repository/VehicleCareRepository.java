package com.example.vehicleService.repository;

import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.VehicleCare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleCareRepository extends JpaRepository<VehicleCare, Long> {
    List<VehicleCare> findByShop(Shop shop);
    List<VehicleCare> findByNameContainingIgnoreCase(String name);
    List<VehicleCare> findByNameContainingIgnoreCaseAndPriceIsGreaterThanEqualAndPriceIsLessThanEqual(String name, Long start, Long end);
}
