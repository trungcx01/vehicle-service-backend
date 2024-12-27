package com.example.vehicleService.repository;

import com.example.vehicleService.entity.Appointment;
import com.example.vehicleService.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByCustomerUserUsername(String username);
    List<Appointment> findByVehicleCaresShopUserUsername(String username);
    List<Appointment> findByVehicleCaresShopIdAndCustomerIdAndStatusEquals(Long shopId, Long customerId, Status status);
    @Query(value = "SELECT COUNT(*) FROM appointment a " +
            "JOIN base_service bs ON bs.id = a.id " +
            "WHERE DATE(bs.created_at)  = ?1 AND bs.status = 'FINISHED'", nativeQuery = true)
    long countByDate(LocalDate date);

    @Query(value = "SELECT COUNT(*) FROM appointment a " +
            "JOIN base_service bs ON bs.id = a.id " +
            "JOIN appointment_vehicle_care avc ON avc.appointment_id = a.id " +
            "JOIN vehicle_care vc ON vc.id = avc.vehicle_care_id " +
            "WHERE DATE(bs.created_at)  = ?1 AND bs.status = 'FINISHED' AND vc.shop_id = ?2", nativeQuery = true)
    long countByCurrentShopAndDate(LocalDate date, Long shopId);
}
