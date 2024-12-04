package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.PaymentDTO;
import com.example.vehicleService.dto.VnpayResponseDTO;
import com.example.vehicleService.entity.Appointment;
import com.example.vehicleService.entity.Payment;
import com.example.vehicleService.entity.VehicleCare;
import com.example.vehicleService.entity.enums.PaymentMethod;
import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.mapper.PaymentMapper;
import com.example.vehicleService.repository.AppointmentRepository;
import com.example.vehicleService.repository.EmergencyRequestRepository;
import com.example.vehicleService.repository.PaymentRepository;
import com.example.vehicleService.service.PaymentService;
import com.example.vehicleService.service.VnpayService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final EmergencyRequestRepository emergencyRequestRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final VnpayService vnpayService;

    public PaymentServiceImpl(EmergencyRequestRepository emergencyRequestRepository, AppointmentRepository appointmentRepository, PaymentMapper paymentMapper, PaymentRepository paymentRepository, VnpayService vnpayService) {
        this.emergencyRequestRepository = emergencyRequestRepository;
        this.appointmentRepository = appointmentRepository;
        this.paymentMapper = paymentMapper;
        this.paymentRepository = paymentRepository;
        this.vnpayService = vnpayService;
    }

    @Override
    public Payment getById(Long id) {
        return paymentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Payment!")
        );
    }

    @Override
    public Page<Payment> getAllPagination(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    @Override
    public Payment save(PaymentDTO paymentDTO, HttpServletRequest request) {
        Payment payment = paymentMapper.toEntity(paymentDTO, appointmentRepository, emergencyRequestRepository);
        double amount_ = 0;
        if (paymentDTO.getAppointmentId() == null){
            amount_ = 100000;
        }else{
            for (VehicleCare vehicleCare : payment.getAppointment().getVehicleCares()){
                amount_ += vehicleCare.getPrice();
            }
        }
        payment.setAmount(amount_);
        if (paymentDTO.getPaymentMethod().equals(PaymentMethod.BANKING)){
            payment.setPaymentStatus(Status.PENDING);
           VnpayResponseDTO res = vnpayService.createPayment(request, amount_,
                    "Thanh toan cho don hàng: "
                            + (paymentDTO.getAppointmentId() != null
                            ? "Appointment cho id: " + paymentDTO.getAppointmentId()
                            : "Emergency Request cho id: " + paymentDTO.getEmergencyRequestId()));
            payment.setPayLink(res.getPayLink());
            payment.setTransactionReference(res.getVnp_TxnRef());
        } else payment.setPaymentStatus(Status.ACCEPTED);
        return paymentRepository.save(payment);
    }

    @Override
    public void deleteById(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Payment!")
        );
        paymentRepository.delete(payment);
    }
}
