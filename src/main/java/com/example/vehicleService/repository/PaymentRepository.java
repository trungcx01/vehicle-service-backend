package com.example.vehicleService.repository;

import com.example.vehicleService.entity.Payment;
import com.example.vehicleService.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByTransactionReference(String transactionReference);
    Payment findByBaseServiceId(Long id);

    @Query(value = "SELECT SUM(p.amount)\n" +
            "FROM payment p\n" +
            "JOIN base_service bs ON p.base_service_id = bs.id\n" +
            "JOIN proposal pr ON pr.id = bs.id\n" +
            "JOIN shop s ON pr.shop_id = s.id\n" +
            "WHERE DATE(p.updated_at) = DATE(?1)\n" +
            "  AND p.payment_status = 'FINISHED'\n" +
            "  AND s.id = ?2\n" +
            "  AND p.service_type = 'EMERGENCY_REQUEST';\n", nativeQuery = true)
    Long totalAmountForEmergencyRequestByDateAndShop(LocalDate date, Long shopId);


    @Query(value = "SELECT SUM(p.amount)\n" +
            "FROM payment p\n" +
            "JOIN base_service bs ON p.base_service_id = bs.id\n" +
            "JOIN appointment a ON a.id = bs.id\n" +
            "JOIN appointment_vehicle_care avc ON a.id = avc.appointment_id\n" +
            "JOIN vehicle_care vc ON avc.vehicle_care_id = vc.id\n" +
            "JOIN shop s ON vc.shop_id = s.id\n" +
            "WHERE DATE(p.updated_at) = DATE(?1)\n" +
            "  AND p.payment_status = 'FINISHED'\n" +
            "  AND s.id = ?2\n" +
            "  AND p.service_type = 'APPOINTMENT';\n", nativeQuery = true)
    Long totalAmountForAppointmentByDateAndShop(LocalDate date, Long shopId);

//    Payment findByProposalEmergencyRequestId(Long emergencyRequestId);
//    List<Payment> findByAppointmentVehicleCaresShopIdOrProposalShopIdAndPaymentStatusEquals(Long appointmentVehicleCaresShopId, Long proposalShopId, Status status);

}
