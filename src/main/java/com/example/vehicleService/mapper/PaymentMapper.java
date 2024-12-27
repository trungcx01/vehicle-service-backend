package com.example.vehicleService.mapper;

import com.example.vehicleService.dto.PaymentDTO;
import com.example.vehicleService.entity.Payment;
import com.example.vehicleService.repository.AppointmentRepository;
import com.example.vehicleService.repository.EmergencyRequestRepository;
import com.example.vehicleService.repository.ProposalRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment>{

    Payment toEntity(PaymentDTO dto);


    PaymentDTO toDto(Payment entity);
}
