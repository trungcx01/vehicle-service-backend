package com.example.vehicleService.repository;

import com.example.vehicleService.entity.EmergencyRequest;
import com.example.vehicleService.entity.Proposal;
import com.example.vehicleService.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByEmergencyRequestId(Long emergencyRequestId);
    Proposal findByShopAndEmergencyRequest(Shop shop, EmergencyRequest emergencyRequest);
}
