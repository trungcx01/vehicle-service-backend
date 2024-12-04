package com.example.vehicleService.mapper;

import com.example.vehicleService.dto.PaymentDTO;
import com.example.vehicleService.entity.Payment;
import com.example.vehicleService.repository.AppointmentRepository;
import com.example.vehicleService.repository.EmergencyRequestRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AppointmentMapper.class, EmergencyRequestMapper.class})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment>{
    @Mapping(source = "appointmentId", target = "appointment")
    @Mapping(source = "emergencyRequestId", target = "emergencyRequest")
    Payment toEntity(PaymentDTO dto, @Context AppointmentRepository appointmentRepository, @Context EmergencyRequestRepository emergencyRequestRepository);

    @Mapping(source = "appointment.id", target = "appointmentId")
    @Mapping(source = "emergencyRequest.id", target = "emergencyRequestId")
    PaymentDTO toDto(Payment entity);
}
