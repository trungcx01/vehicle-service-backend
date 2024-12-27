package com.example.vehicleService.repository;

import com.example.vehicleService.entity.EmergencyRequest;
import com.example.vehicleService.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmergencyRequestRepository extends JpaRepository<EmergencyRequest, Long> {
    List<EmergencyRequest> findByRequestStatusEqualsAndCustomerIdAndProposalsShopId(Status status, Long customerId, Long shopId);
    @Query(value = "SELECT COUNT(*) FROM proposal p " +
            "JOIN base_service bs ON bs.id = p.id " +
            "WHERE DATE(bs.created_at)  = ?1 AND bs.status = 'ACCEPTED'", nativeQuery = true)
    long countByDate(LocalDate date);

    @Query(value = "SELECT COUNT(*) FROM proposal p " +
            "JOIN base_service bs ON bs.id = p.id " +
            "WHERE DATE(bs.created_at)  = ?1 AND bs.status = 'ACCEPTED' AND p.shop_id = ?2", nativeQuery = true)
    long countByDateAndCurrentShop(LocalDate date, Long shopId);
    List<EmergencyRequest> findByCustomerUserUsername(String username);
}
