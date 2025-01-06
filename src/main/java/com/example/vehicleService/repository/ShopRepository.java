package com.example.vehicleService.repository;

import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.VehicleCare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Integer> {
    Shop findByUserUsername(String username);
    @Query("SELECT s FROM Shop s ORDER BY s.rating DESC")
    List<Shop> findTop6ByOrderByRatingDesc();

    @Query(value = "SELECT * FROM shop s " +
            "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', ?1, '%'))  and LOWER(s.district) LIKE LOWER(CONCAT('%', ?2, '%')) and s.rating >= ?3", nativeQuery = true)
    List<Shop> searchShop(String name, String district, Double rating);

    @Query(value = "SELECT * FROM shop s " +
            "ORDER BY revenue DESC " +
            "LIMIT 10",
            nativeQuery = true)
    List<Shop> findTop10Revenue();


    @Query(value = """
        SELECT s.* 
        FROM shop s
        LEFT JOIN user u ON s.user_id = u.id
        WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
           OR LOWER(s.address) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
           OR LOWER(s.phone_number) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
    """, countQuery = """
        SELECT COUNT(*) 
        FROM shop s
        LEFT JOIN user u ON s.user_id = u.id
        WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
           OR LOWER(s.address) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
           OR LOWER(s.phone_number) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
    """, nativeQuery = true)
    Page<Shop> searchShopsNative(@Param("searchTerm") String searchTerm, Pageable pageable);
}
