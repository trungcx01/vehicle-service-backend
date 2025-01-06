package com.example.vehicleService.mapper;

import com.example.vehicleService.dto.AppointmentDTO;
import com.example.vehicleService.entity.Appointment;
import com.example.vehicleService.entity.VehicleCare;
import com.example.vehicleService.repository.AppointmentRepository;
import com.example.vehicleService.repository.CustomerRepository;
import com.example.vehicleService.repository.VehicleCareRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public interface AppointmentMapper extends EntityMapper<AppointmentDTO, Appointment> {
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "vehicleCareIds", target = "vehicleCares", qualifiedByName = "toSetVehicleCares")
    Appointment toEntity(AppointmentDTO dto, @Context CustomerRepository customerRepository, @Context VehicleCareRepository vehicleCareRepository);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "vehicleCares", target = "vehicleCareIds", qualifiedByName = "toSetVehicleCareIds")
    AppointmentDTO toDto(Appointment entity);

    @Named("toSetVehicleCares")
    default Set<VehicleCare> toSetVehicleCares(Set<Integer> vehicleCareIds, @Context VehicleCareRepository vehicleCareRepository){
        return vehicleCareIds.stream().map(
                        id -> vehicleCareRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found Vehicle Care!")))
                .collect(Collectors.toSet());
    }

    @Named("toSetVehicleCareIds")
    default Set<Integer> toSetVehicleCareIds(Set<VehicleCare> vehicleCares){
        return vehicleCares.stream().map(vehicleCare -> vehicleCare.getId()).collect(Collectors.toSet());
    }

    default Appointment fromId(Integer id, @Context AppointmentRepository appointmentRepository){
        if (id == null){
            return null;
        }
        return appointmentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Appointment!")
        );
    }
}
