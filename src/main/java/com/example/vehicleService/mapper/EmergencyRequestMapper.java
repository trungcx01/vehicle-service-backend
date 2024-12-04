package com.example.vehicleService.mapper;

import com.example.vehicleService.dto.EmergencyRequestDTO;
import com.example.vehicleService.entity.EmergencyRequest;
import com.example.vehicleService.entity.Proposal;
import com.example.vehicleService.exception.BlogAPIException;
import com.example.vehicleService.repository.CustomerRepository;
import com.example.vehicleService.repository.EmergencyRequestRepository;
import com.example.vehicleService.repository.ProposalRepository;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public interface EmergencyRequestMapper extends EntityMapper<EmergencyRequestDTO, EmergencyRequest> {
//    @Mapping(source = "proposalIds", target = "proposals", qualifiedByName = "idsToEntitiesProposal")
    @Mapping(source = "customerId", target = "customer")
    EmergencyRequest toEntity(EmergencyRequestDTO dto, @Context ProposalRepository proposalRepository, @Context CustomerRepository customerRepository);

//    @Mapping(source = "proposals", target = "proposalIds", qualifiedByName = "entitiesToIdsProposal")
    @Mapping(source = "customer.id", target = "customerId")
    EmergencyRequestDTO toDto(EmergencyRequest entity);

    default EmergencyRequest fromId(Long id, @Context EmergencyRequestRepository emergencyRequestRepository){
        if (id == null){
            return null;
        }
        return emergencyRequestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Emergency Request!")
        );
    }

//    @Named("idsToEntitiesProposal")
//    default Set<Proposal> idsToEntitiesProposal(Set<Long> proposalIds, @Context ProposalRepository proposalRepository){
//        return proposalIds.stream().map(p -> proposalRepository.findById(p).orElseThrow(
//                () -> new EntityNotFoundException("Not found proposal!")
//        )).collect(Collectors.toSet());
//    }
//
//    @Named("entitiesToIdsProposal")
//    default Set<Long> entitiesToIdsProposal(Set<Proposal> proposals){
//        return proposals.stream().map(p -> p.getId()).collect(Collectors.toSet());
//    }
}
