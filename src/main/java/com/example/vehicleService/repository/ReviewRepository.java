package com.example.vehicleService.repository;

import com.example.vehicleService.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT r FROM Review r ORDER BY r.createAt DESC")
    List<Review> findTop8ByOrderByCreatedAtDesc();

    Review findByBaseServiceId(Integer id);

    @Query(value = "SELECT r.* FROM review r \n" +
            "JOIN base_service bs ON r.base_service_id = bs.id \n" +
            "LEFT JOIN appointment a ON a.id = bs.id \n" +
            "LEFT JOIN appointment_vehicle_care avc ON avc.appointment_id = a.id \n" +
            "LEFT JOIN vehicle_care vc ON vc.id = avc.vehicle_care_id \n" +
            "LEFT JOIN proposal p ON p.id = bs.id \n" +
            "WHERE vc.shop_id = :shopId OR p.shop_id = :shopId", nativeQuery = true)
    List<Review> findByShopId(Integer shopId);

    @Query(value = """
    SELECT r.* 
    FROM review r
    JOIN base_service bs ON r.base_service_id = bs.id
    LEFT JOIN appointment a ON a.id = bs.id
    LEFT JOIN appointment_vehicle_care avc ON avc.appointment_id = a.id
    LEFT JOIN vehicle_care vc ON vc.id = avc.vehicle_care_id
    LEFT JOIN proposal p ON p.id = bs.id
    WHERE (vc.shop_id = :shopId OR p.shop_id = :shopId)
""", countQuery = """
    SELECT COUNT(*) 
    FROM review r
    JOIN base_service bs ON r.base_service_id = bs.id
    LEFT JOIN appointment a ON a.id = bs.id
    LEFT JOIN appointment_vehicle_care avc ON avc.appointment_id = a.id
    LEFT JOIN vehicle_care vc ON vc.id = avc.vehicle_care_id
    LEFT JOIN proposal p ON p.id = bs.id
    WHERE (vc.shop_id = :shopId OR p.shop_id = :shopId)
""", nativeQuery = true)
    Page<Review> findByShopId(@Param("shopId") Integer shopId, Pageable pageable);


    @Query(value = """
        SELECT r.*
        FROM review r
        INNER JOIN base_service b ON r.base_service_id = b.id
        LEFT JOIN appointment a ON b.id = a.id
        LEFT JOIN customer c ON a.customer_id = c.id
        LEFT JOIN proposal p ON b.id = p.id
        LEFT JOIN emergency_request er ON p.emergency_request_id = er.id
        LEFT JOIN customer c2 ON er.customer_id = c2.id
        WHERE (
               (r.service_type = 'APPOINTMENT' AND (
                   LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(a.vehicle_type) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               ))
            OR
               (r.service_type = 'EMERGENCY_REQUEST' AND (
                   LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(c2.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(er.location) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(er.license_plate) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(er.vehicle_type) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               ))
        )
    """, countQuery = """
        SELECT COUNT(*)
        FROM review r
        INNER JOIN base_service b ON r.base_service_id = b.id
        LEFT JOIN appointment a ON b.id = a.id
        LEFT JOIN customer c ON a.customer_id = c.id
        LEFT JOIN proposal p ON b.id = p.id
        LEFT JOIN emergency_request er ON p.emergency_request_id = er.id
        LEFT JOIN customer c2 ON er.customer_id = c2.id
        WHERE (
               (r.service_type = 'APPOINTMENT' AND (
                   LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(a.vehicle_type) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               ))
            OR
               (r.service_type = 'EMERGENCY_REQUEST' AND (
                   LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(c2.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(er.location) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(er.license_plate) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(er.vehicle_type) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               ))
        )
    """, nativeQuery = true)
    Page<Review> searchReviews(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query(value = """
    SELECT r.*
    FROM review r
    INNER JOIN base_service b ON r.base_service_id = b.id
    LEFT JOIN appointment a ON b.id = a.id
    LEFT JOIN customer c ON a.customer_id = c.id
    LEFT JOIN appointment_vehicle_care avc ON a.id = avc.appointment_id
    LEFT JOIN vehicle_care vc ON avc.vehicle_care_id = vc.id
    LEFT JOIN shop s ON vc.shop_id = s.id
    LEFT JOIN proposal p ON b.id = p.id
    LEFT JOIN emergency_request er ON p.emergency_request_id = er.id
    LEFT JOIN customer c2 ON er.customer_id = c2.id
    LEFT JOIN shop s2 ON p.shop_id = s2.id
    WHERE (
        (r.service_type = 'APPOINTMENT' AND (
            LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(a.vehicle_type) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(vc.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        ) AND s.id = :shopId)
        OR
        (r.service_type = 'EMERGENCY_REQUEST' AND (
            LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(c2.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(er.location) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(er.license_plate) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(er.vehicle_type) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(s2.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        ) AND s2.id = :shopId)
    )
""", countQuery = """
    SELECT COUNT(*)
    FROM review r
    INNER JOIN base_service b ON r.base_service_id = b.id
    LEFT JOIN appointment a ON b.id = a.id
    LEFT JOIN customer c ON a.customer_id = c.id
    LEFT JOIN appointment_vehicle_care avc ON a.id = avc.appointment_id
    LEFT JOIN vehicle_care vc ON avc.vehicle_care_id = vc.id
    LEFT JOIN shop s ON vc.shop_id = s.id
    LEFT JOIN proposal p ON b.id = p.id
    LEFT JOIN emergency_request er ON p.emergency_request_id = er.id
    LEFT JOIN customer c2 ON er.customer_id = c2.id
    LEFT JOIN shop s2 ON p.shop_id = s2.id
    WHERE (
        (r.service_type = 'APPOINTMENT' AND (
            LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(a.vehicle_type) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(vc.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        ) AND s.id = :shopId)
        OR
        (r.service_type = 'EMERGENCY_REQUEST' AND (
            LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(c2.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(er.location) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(er.license_plate) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(er.vehicle_type) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(s2.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        ) AND s2.id = :shopId)
    )
""", nativeQuery = true)
    Page<Review> searchReviewsInShop(@Param("searchTerm") String searchTerm, @Param("shopId") Integer shopId, Pageable pageable);


}
