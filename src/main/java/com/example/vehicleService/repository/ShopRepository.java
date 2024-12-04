package com.example.vehicleService.repository;

import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.VehicleCare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    Shop findByUserUsername(String username);
    @Query("SELECT s FROM Shop s ORDER BY s.rating DESC")
    List<Shop> findTop6ByOrderByRatingDesc();

    List<Shop> findByNameContainingIgnoreCase(String name);
}
