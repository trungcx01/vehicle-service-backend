package com.example.vehicleService.repository;

import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.VehicleCare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleCareRepository extends JpaRepository<VehicleCare, Integer> {
    Page<VehicleCare> findByShop(Shop shop, Pageable pageable);
    List<VehicleCare> findByShop(Shop shop);

    @Query(value = "SELECT vc.* FROM vehicle_care vc " +
            "JOIN shop s ON vc.shop_id = s.id " +
            "WHERE LOWER(vc.name) LIKE LOWER(CONCAT('%', ?1, '%')) " +
            "AND LOWER(s.district) LIKE LOWER(CONCAT('%', ?2, '%')) " +
            "AND vc.price >= ?3 AND vc.price <= ?4", nativeQuery = true)
    List<VehicleCare> searchVehicleCare(String name, String district, Integer startPrice, Integer endPrice);

    @Query(value = """
        SELECT v.* 
        FROM vehicle_care v
        WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
           OR LOWER(v.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
    """, countQuery = """
        SELECT COUNT(*) 
        FROM vehicle_care v
        WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
           OR LOWER(v.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
    """, nativeQuery = true)
    Page<VehicleCare> searchVehicleCares(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query(value = """
        SELECT v.* 
        FROM vehicle_care v
        WHERE v.shop_id = :shopId
          AND (
            LOWER(v.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(v.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
          )
    """,
            countQuery = """
        SELECT COUNT(*) 
        FROM vehicle_care v
        WHERE v.shop_id = :shopId
          AND (
            LOWER(v.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(v.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
          )
    """,
            nativeQuery = true)
    Page<VehicleCare> searchVehicleCaresByShop(@Param("searchTerm") String searchTerm, @Param("shopId") Integer shopId, Pageable pageable);

}
