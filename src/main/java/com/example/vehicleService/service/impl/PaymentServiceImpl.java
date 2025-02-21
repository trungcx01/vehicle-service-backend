package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.PaymentDTO;
import com.example.vehicleService.dto.VnpayResponseDTO;
import com.example.vehicleService.entity.*;
import com.example.vehicleService.entity.enums.PaymentMethod;
import com.example.vehicleService.entity.enums.ServiceType;
import com.example.vehicleService.entity.enums.Status;
import com.example.vehicleService.exception.BlogAPIException;
import com.example.vehicleService.mapper.PaymentMapper;
import com.example.vehicleService.repository.*;
import com.example.vehicleService.service.PaymentService;
import com.example.vehicleService.service.VnpayService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Payment getById(Integer id) {
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
        Payment payment1 = null;
        if (paymentDTO.getProposalId() != null){
             payment1 = paymentRepository.findByBaseServiceId(paymentDTO.getProposalId());
        }else if(paymentDTO.getAppointmentId() != null){
             payment1 = paymentRepository.findByBaseServiceId(paymentDTO.getAppointmentId());
        }
        if (payment1 != null && !payment1.getStatus().equals(Status.FINISHED)){
            paymentRepository.delete(payment1);
        }
        Payment payment = paymentMapper.toEntity(paymentDTO);
        long amount_ = 0;
        Shop shop = null;
        Appointment appointment = null;
        Proposal proposal = null;
        if (paymentDTO.getAppointmentId() == null){
            payment.setServiceType(ServiceType.EMERGENCY_REQUEST);
            proposal = proposalRepository.findById(paymentDTO.getProposalId()).orElseThrow(
                    () -> new EntityNotFoundException("Not found proposal!")
            );
            amount_ = 100000;
            shop = proposal.getShop();
            payment.setBaseService(proposal);
        }else{
            payment.setServiceType(ServiceType.APPOINTMENT);
            appointment = appointmentRepository.findById(paymentDTO.getAppointmentId()).orElseThrow(
                    () -> new EntityNotFoundException("Not found appointment!")
            );
            payment.setBaseService(appointment);
            for (VehicleCare vehicleCare : appointment.getVehicleCares()){
                amount_ += vehicleCare.getPrice() == null ? 0 : vehicleCare.getPrice();
                shop = vehicleCare.getShop();
            }
        }
        payment.setAmount(amount_);
        if (paymentDTO.getPaymentMethod().equals(PaymentMethod.BANKING)){
            if (appointment != null){
                for (VehicleCare v : appointment.getVehicleCares()){
                    if (v.getPrice() == null){
                        throw new BlogAPIException(HttpStatus.BAD_REQUEST,
                                "Không được phép thanh toán online với lịch hẹn chứa Hạng mục không có giá cụ thể");
                    }
                }
            }
            payment.setStatus(Status.PENDING);
           VnpayResponseDTO res = vnpayService.createPayment(request, amount_,
                    "Thanh toan cho don hang: "
                            + (paymentDTO.getAppointmentId() != null
                            ? "Appointment cho id: " + paymentDTO.getAppointmentId()
                            : "Proposal cho id: " + paymentDTO.getProposalId()));
            payment.setPayLink(res.getPayLink());
            payment.setTransactionReference(res.getVnp_TxnRef());
        } else {
            payment.setStatus(Status.FINISHED);
            payment.setOrderInfo("Thanh toan cho don hang: "
                    + (paymentDTO.getAppointmentId() != null
                    ? "Appointment cho id " + paymentDTO.getAppointmentId()
                    : "Proposal cho id " + paymentDTO.getProposalId()));
            if (shop != null){
                shop.setRevenue(shop.getRevenue() != null ? shop.getRevenue() + amount_ : shop.getRevenue());
            }
        }
        return paymentRepository.save(payment);
    }

    @Override
    public void deleteById(Integer id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Payment!")
        );
        paymentRepository.delete(payment);
    }

//    @Override
//    public Payment getPaymentOfRequest(Integer id) {
//        return paymentRepository.findByProposalEmergencyRequestId(id);
//    }

    @Override
    public Payment getPaymentOfAppointment(Integer id) {
        return paymentRepository.findByBaseServiceId(id);
    }

//    @Override
//    public List<Payment> getFinishedByShop() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Shop shop = shopRepository.findByUserUsername(username);
//        Integer shopId = shop.getId();
//        return paymentRepository.findByAppointmentVehicleCaresShopIdOrProposalShopIdAndPaymentStatusEquals(shopId, shopId, Status.FINISHED);
//    }


    @Override
    public Integer totalAmountByDateAndCurrentShop(LocalDate date) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Shop shop = shopRepository.findByUserUsername(username);

        Integer appointmentAmount = Optional.ofNullable(paymentRepository.totalAmountForAppointmentByDateAndShop(date, shop.getId())).orElse(0);
        Integer emergencyRequestAmount = Optional.ofNullable(paymentRepository.totalAmountForEmergencyRequestByDateAndShop(date, shop.getId())).orElse(0);

        return appointmentAmount + emergencyRequestAmount;
    }

    @Override
    public Long getTotalRevenue() {
        return paymentRepository.getTotalRevenue();
    }
}
