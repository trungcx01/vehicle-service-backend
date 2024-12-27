package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.PaymentDTO;
import com.example.vehicleService.dto.VnpayResponseDTO;
import com.example.vehicleService.entity.*;
import com.example.vehicleService.entity.enums.PaymentMethod;
import com.example.vehicleService.entity.enums.ServiceType;
import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.mapper.PaymentMapper;
import com.example.vehicleService.repository.*;
import com.example.vehicleService.service.PaymentService;
import com.example.vehicleService.service.VnpayService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final ProposalRepository proposalRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final VnpayService vnpayService;
    private final ShopRepository shopRepository;

    public PaymentServiceImpl(ProposalRepository proposalRepository, AppointmentRepository appointmentRepository, PaymentMapper paymentMapper, PaymentRepository paymentRepository, VnpayService vnpayService, ShopRepository shopRepository) {
        this.proposalRepository = proposalRepository;
        this.appointmentRepository = appointmentRepository;
        this.paymentMapper = paymentMapper;
        this.paymentRepository = paymentRepository;
        this.vnpayService = vnpayService;
        this.shopRepository = shopRepository;
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
        Payment payment = paymentMapper.toEntity(paymentDTO);
        double amount_ = 0;
        if (paymentDTO.getAppointmentId() == null){
            payment.setServiceType(ServiceType.EMERGENCY_REQUEST);
            amount_ = 100000;
            Proposal proposal = proposalRepository.findById(paymentDTO.getProposalId()).orElseThrow(
                    () -> new EntityNotFoundException("Not found proposal!")
            );
            payment.setBaseService(proposal);
        }else{
            payment.setServiceType(ServiceType.APPOINTMENT);
            Appointment appointment = appointmentRepository.findById(paymentDTO.getAppointmentId()).orElseThrow(
                    () -> new EntityNotFoundException("Not found appointment!")
            );
            payment.setBaseService(appointment);
            for (VehicleCare vehicleCare : appointment.getVehicleCares()){
                amount_ += vehicleCare.getPrice();
            }
        }
        payment.setAmount(amount_);
        if (paymentDTO.getPaymentMethod().equals(PaymentMethod.BANKING)){
            payment.setStatus(Status.PENDING);
           VnpayResponseDTO res = vnpayService.createPayment(request, amount_,
                    "Thanh toan cho don hang: "
                            + (paymentDTO.getAppointmentId() != null
                            ? "Appointment cho id: " + paymentDTO.getAppointmentId()
                            : "Proposal cho id: " + paymentDTO.getProposalId()));
            payment.setPayLink(res.getPayLink());
            payment.setTransactionReference(res.getVnp_TxnRef());
        } else payment.setStatus(Status.FINISHED);
        return paymentRepository.save(payment);
    }

    @Override
    public void deleteById(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Payment!")
        );
        paymentRepository.delete(payment);
    }

//    @Override
//    public Payment getPaymentOfRequest(Long id) {
//        return paymentRepository.findByProposalEmergencyRequestId(id);
//    }

    @Override
    public Payment getPaymentOfAppointment(Long id) {
        return paymentRepository.findByBaseServiceId(id);
    }

//    @Override
//    public List<Payment> getFinishedByShop() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Shop shop = shopRepository.findByUserUsername(username);
//        Long shopId = shop.getId();
//        return paymentRepository.findByAppointmentVehicleCaresShopIdOrProposalShopIdAndPaymentStatusEquals(shopId, shopId, Status.FINISHED);
//    }


    @Override
    public Long totalAmountByDateAndCurrentShop(LocalDate date) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Shop shop = shopRepository.findByUserUsername(username);

        Long appointmentAmount = Optional.ofNullable(paymentRepository.totalAmountForAppointmentByDateAndShop(date, shop.getId())).orElse(0L);
        Long emergencyRequestAmount = Optional.ofNullable(paymentRepository.totalAmountForEmergencyRequestByDateAndShop(date, shop.getId())).orElse(0L);

        return appointmentAmount + emergencyRequestAmount;
    }

}
