package com.example.vehicleService.repository;

import com.example.vehicleService.entity.EmergencyRequest;
import com.example.vehicleService.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmergencyRequestRepository extends JpaRepository<EmergencyRequest, Integer> {
    List<EmergencyRequest> findByRequestStatusEqualsAndCustomerIdAndProposalsShopId(Status status, Integer customerId, Integer shopId);
    @Query(value = "SELECT COUNT(*) FROM proposal p " +
            "JOIN base_service bs ON bs.id = p.id " +
            "WHERE DATE(bs.created_at)  = ?1 AND bs.status = 'ACCEPTED'", nativeQuery = true)
    Integer countByDate(LocalDate date);

    @Query(value = "SELECT COUNT(*) FROM proposal p " +
            "JOIN base_service bs ON bs.id = p.id " +
            "WHERE DATE(bs.created_at)  = ?1 AND bs.status = 'ACCEPTED' AND p.shop_id = ?2", nativeQuery = true)
    Integer countByDateAndCurrentShop(LocalDate date, Integer shopId);
    List<EmergencyRequest> findByCustomerUserUsername(String username);
    @Query(value = "SELECT COUNT(*) FROM proposal p " +
            "JOIN base_service bs ON bs.id = p.id " +
            "WHERE bs.status = 'ACCEPTED' AND p.shop_id = ?1", nativeQuery = true)
    long countByCurrentShop(Integer shopId);

    @Query(value = "SELECT COUNT(*) FROM proposal p " +
            "JOIN base_service bs ON bs.id = p.id " +
            "WHERE bs.status = 'ACCEPTED'", nativeQuery = true)
    long count();


    @Query(value = """
        SELECT er.*
        FROM emergency_request er
        INNER JOIN customer c ON er.customer_id = c.id
        WHERE (LOWER(er.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
           OR (LOWER(er.location) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
           OR (LOWER(er.license_plate) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
           OR (LOWER(er.vehicle_type) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
           OR (LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
    """, countQuery = """
        SELECT COUNT(*) 
        FROM emergency_request er
        INNER JOIN customer c ON er.customer_id = c.id
        WHERE (LOWER(er.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
           OR (LOWER(er.location) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
           OR (LOWER(er.license_plate) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
           OR (LOWER(er.vehicle_type) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
           OR (LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
    """, nativeQuery = true)
    Page<EmergencyRequest> searchEmergencyRequests(@Param("searchTerm") String searchTerm, Pageable pageable);
}
