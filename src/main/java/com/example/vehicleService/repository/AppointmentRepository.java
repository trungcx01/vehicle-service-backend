package com.example.vehicleService.repository;

import com.example.vehicleService.entity.Appointment;
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
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByCustomerUserUsername(String username);
    Page<Appointment> findByVehicleCaresShopUserUsername(String username, Pageable pageable);
    List<Appointment> findByVehicleCaresShopIdAndCustomerIdAndStatusEquals(Integer shopId, Integer customerId, Status status);
    @Query(value = "SELECT COUNT(*) FROM appointment a " +
            "JOIN base_service bs ON bs.id = a.id " +
            "WHERE DATE(bs.created_at)  = ?1 AND bs.status = 'FINISHED'", nativeQuery = true)
    Integer countByDate(LocalDate date);

    @Query(value = "SELECT COUNT(*) FROM appointment a " +
            "JOIN base_service bs ON bs.id = a.id " +
            "JOIN appointment_vehicle_care avc ON avc.appointment_id = a.id " +
            "JOIN vehicle_care vc ON vc.id = avc.vehicle_care_id " +
            "WHERE DATE(bs.created_at)  = ?1 AND bs.status = 'FINISHED' AND vc.shop_id = ?2", nativeQuery = true)
    Integer countByCurrentShopAndDate(LocalDate date, Integer shopId);

    @Query(value = "SELECT COUNT(*) FROM appointment a " +
            "JOIN base_service bs ON bs.id = a.id " +
            "JOIN appointment_vehicle_care avc ON avc.appointment_id = a.id " +
            "JOIN vehicle_care vc ON vc.id = avc.vehicle_care_id " +
            "WHERE bs.status = 'FINISHED' AND vc.shop_id = ?1", nativeQuery = true)
    Integer countByCurrentShop(Integer shopId);

    @Query(value = "SELECT COUNT(*) FROM appointment a " +
            "JOIN base_service bs ON bs.id = a.id " +
            "WHERE bs.status = 'FINISHED'", nativeQuery = true)
    long count();

    @Query(value = """
        SELECT a.*, b.created_at, b.updated_at, b.status
        FROM appointment a
        INNER JOIN base_service b ON a.id = b.id
        INNER JOIN customer c ON a.customer_id = c.id
        WHERE (LOWER(a.note) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
           OR (LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
           OR (LOWER(a.vehicle_type) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
    """, countQuery = """
        SELECT COUNT(*) 
        FROM appointment a
        INNER JOIN base_service b ON a.id = b.id
        INNER JOIN customer c ON a.customer_id = c.id
        WHERE (LOWER(a.note) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
           OR (LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
           OR (LOWER(a.vehicle_type) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
    """, nativeQuery = true)
    Page<Appointment> searchAppointments(@Param("searchTerm") String searchTerm, Pageable pageable);


    @Query(value = """
        SELECT a.*, b.created_at, b.updated_at, b.status
        FROM appointment a
        INNER JOIN base_service b ON a.id = b.id
        INNER JOIN customer c ON a.customer_id = c.id
        INNER JOIN appointment_vehicle_care avc ON a.id = avc.appointment_id
        INNER JOIN vehicle_care vc ON avc.vehicle_care_id = vc.id
        INNER JOIN shop s ON vc.shop_id = s.id
        WHERE s.id = :shopId
          AND (
            LOWER(a.note) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(a.vehicle_type) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
          )
    """, countQuery = """
        SELECT COUNT(*) 
        FROM appointment a
        INNER JOIN base_service b ON a.id = b.id
        INNER JOIN customer c ON a.customer_id = c.id
        INNER JOIN appointment_vehicle_care avc ON a.id = avc.appointment_id
        INNER JOIN vehicle_care vc ON avc.vehicle_care_id = vc.id
        INNER JOIN shop s ON vc.shop_id = s.id
        WHERE s.id = :shopId
          AND (
            LOWER(a.note) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            OR LOWER(a.vehicle_type) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
          )
    """, nativeQuery = true)
    Page<Appointment> searchAppointmentsInShop(@Param("searchTerm") String searchTerm, @Param("shopId") Integer shopId, Pageable pageable
    );

}
